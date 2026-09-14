package ru.wolfbertfx.houston.control.venue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Type;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;
import ru.wolfbertfx.houston.control.venue.domain.CalendarException;
import ru.wolfbertfx.houston.control.venue.domain.CalendarExceptionRepository;
import ru.wolfbertfx.houston.control.venue.domain.InstrumentSession;
import ru.wolfbertfx.houston.control.venue.domain.InstrumentSessionRepository;
import ru.wolfbertfx.houston.control.venue.domain.VenueSession;
import ru.wolfbertfx.houston.control.venue.domain.VenueSessionRepository;
import ru.wolfbertfx.houston.control.venue.infra.cache.VenueCacheAdapter;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class VenueService {

    private static final Logger log = LoggerFactory.getLogger(VenueService.class);

    @Inject
    VenueSessionRepository sessionRepo;

    @Inject
    InstrumentSessionRepository instSessionRepo;

    @Inject
    CalendarExceptionRepository excRepo;

    @Inject
    VenueCacheAdapter cacheAdapter;

    @ConfigProperty(name = "venue.scheduler.interval-minutes", defaultValue = "1")
    int schedulerIntervalMinutes;

    /**
     * Main scheduler entry point - runs every minute.
     * Recalculates venue states for all instruments and updates Redis sets.
     */
    @jakarta.annotation.PostConstruct
    void onStartup() {
        log.info("VenueService started, scheduler interval: {} min", schedulerIntervalMinutes);
        recalcAllVenueStates();
    }

    /**
     * Scheduled recalculation of all venue states.
     * Runs every minute to keep Redis sets in sync with current trading sessions.
     */
    @io.quarkus.scheduler.Scheduled(cron = "0 * * * * ?", identity = "venue-state-recalc")
    void recalcAllVenueStates() {
        try {
            Instant now = Instant.now();
            log.debug("Recalculating venue states at {}", now);

            for (Venue venue : Venue.values()) {
                recalcVenueState(venue, now);
            }
        } catch (Exception e) {
            log.error("Failed to recalc venue states", e);
        }
    }

    /**
     * Recalculates state for a single venue and updates Redis sets via cache adapter.
     */
    @Transactional
    void recalcVenueState(Venue venue, Instant now) {
        ZoneId venueTz = ZoneId.of(venue.getTimeZone());
        ZonedDateTime nowInVenue = now.atZone(venueTz);
        LocalDate today = nowInVenue.toLocalDate();
        int dow = today.getDayOfWeek().getValue(); // 1=MON
        LocalTime nowTime = nowInVenue.toLocalTime();

        // 1. Base venue sessions for today
        List<VenueSession> baseSessions = sessionRepo.findByVenueAndDay(venue, dow);
        if (baseSessions.isEmpty()) {
            log.debug("No base sessions for venue {} on day {}", venue, dow);
            return;
        }

        // 2. Instrument session overrides
        List<InstrumentSession> instSessions = instSessionRepo.findByVenueAndDay(venue, dow);
        Map<Integer, InstrumentSession> byInstrument = instSessions.stream()
                .filter(s -> s.instrument() != null)
                .collect(Collectors.toMap(s -> s.instrument().getId(), s -> s, (a, b) -> a));
        Map<Integer, InstrumentSession> byType = instSessions.stream()
                .filter(s -> s.type() != null)
                .collect(Collectors.toMap(s -> s.type().getId(), s -> s, (a, b) -> a));

        // 3. Calendar exceptions for today
        List<CalendarException> exceptions = excRepo.findByVenueAndDateRange(venue, today, today);

        // 4. Get all instruments for this venue
        List<Instrument> instruments = Arrays.stream(Instrument.values())
                .filter(i -> i.getVenue() == venue)
                .toList();

        int updatedLive = 0, removedLive = 0;

        for (Instrument instrument : instruments) {
            // 1. Pick base session for current time
            Optional<VenueSession> baseWindow = pickWindow(baseSessions, nowTime);
            if (baseWindow.isEmpty()) continue;

            Phase effectivePhase = baseWindow.get().phase();

            // 2. Apply instrument session override
            InstrumentSession override = byInstrument.get(instrument.getId());
            if (override == null && instrument.getType() != null) {
                override = byType.get(instrument.getType().getId());
            }
            if (override != null) {
                Optional<InstrumentSession> overrideWindow = pickWindowOverride(override, nowTime);
                if (overrideWindow.isPresent()) {
                    effectivePhase = overrideWindow.get().phase();
                }
            }

            // 3. Apply calendar exceptions (instrument-specific first, then type, then venue-wide)
            for (CalendarException exc : exceptions) {
                if (exc.covers(instrument)) {
                    if (exc.type() == CalendarException.ExceptionType.FULL_CLOSE) {
                        effectivePhase = Phase.CLOSED;
                    } else if (exc.type() == CalendarException.ExceptionType.REDUCED_HOURS) {
                        // TODO: apply custom schedule from exception
                        effectivePhase = Phase.CLOSED;
                    } else if (exc.type() == CalendarException.ExceptionType.CUSTOM_SCHEDULE) {
                        // TODO: apply custom schedule from exception
                        effectivePhase = Phase.CLOSED;
                    }
                    break;
                }
            }

            // 4. Update Redis sets via cache adapter based on effective phase
            boolean isOpen = (effectivePhase == Phase.OPEN);
            boolean isPreparing = (effectivePhase == Phase.PRE_MARKET);

            // LIVE set (OPEN only)
            if (isOpen) {
                cacheAdapter.addToLive(venue, instrument);
            } else {
                cacheAdapter.removeFromLive(venue, instrument);
            }

            // PREPARING set (PRE_MARKET)
            if (effectivePhase == Phase.PRE_MARKET) {
                cacheAdapter.addToPreparing(venue, instrument);
            } else {
                cacheAdapter.removeFromPreparing(venue, instrument);
            }
        }
    }

    /**
     * Picks the active window from base sessions for current time.
     */
    private Optional<VenueSession> pickWindow(List<VenueSession> sessions, LocalTime now) {
        return sessions.stream()
                .filter(s -> {
                    if (s.isOvernight()) {
                        // e.g., 22:00 - 04:00
                        return !now.isBefore(s.openTime()) || now.isBefore(s.closeTime());
                    } else {
                        // normal day session
                        return !now.isBefore(s.openTime()) && now.isBefore(s.closeTime());
                    }
                })
                .findFirst();
    }

    private Optional<InstrumentSession> pickWindowOverride(InstrumentSession session, LocalTime now) {
        if (session.isOvernight()) {
            return (!now.isBefore(session.openTime()) || now.isBefore(session.closeTime()))
                    ? Optional.of(session) : Optional.empty();
        } else {
            return (!now.isBefore(session.openTime()) && now.isBefore(session.closeTime()))
                    ? Optional.of(session) : Optional.empty();
        }
    }

    // TODO: Add InstrumentRepository injection and use instead of static Instrument.values()
    // TODO: Handle exception custom schedules (REDUCED_HOURS, CUSTOM_SCHEDULE)
    // TODO: Handle PRE_MARKET / POST_MARKET phases
    // TODO: Add metrics / health check
}