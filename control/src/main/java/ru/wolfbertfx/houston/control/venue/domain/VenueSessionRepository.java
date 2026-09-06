package ru.wolfbertfx.houston.control.venue.domain;

import ru.wolfbertfx.houston.common.venue.Phase;
import ru.wolfbertfx.houston.common.venue.Venue;

import java.util.List;
import java.util.Optional;

/**
 * Порт для работы с базовыми сессиями площадок.
 */
public interface VenueSessionRepository {
    List<VenueSession> findByVenue(Venue venue);
    Optional<VenueSession> findByVenueAndDayAndPhase(Venue venue, int dayOfWeek, Phase phase);
    VenueSession save(VenueSession session);
    void delete(VenueSession session);
}