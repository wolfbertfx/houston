package ru.wolfbertfx.houston.control.venue.infra.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;
import ru.wolfbertfx.houston.control.venue.domain.VenueSession;
import ru.wolfbertfx.houston.control.venue.domain.VenueSessionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class VenueSessionRepositoryAdapter implements VenueSessionRepository {

    @Override
    public List<VenueSession> findByVenue(Venue venue) {
        return VenueSessionEntity.find("venue", venue).stream()
                .map(e -> toDomain((VenueSessionEntity) e))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VenueSession> findByVenueAndDayAndPhase(Venue venue, int dayOfWeek, Phase phase) {
        return VenueSessionEntity.find("venue = ?1 and dayOfWeek = ?2 and phase = ?3", venue, dayOfWeek, phase)
                .firstResultOptional()
                .map(e -> toDomain((VenueSessionEntity) e));
    }

    @Override
    @Transactional
    public VenueSession save(VenueSession session) {
        VenueSessionEntity entity;
        if (session.id() != null) {
            entity = VenueSessionEntity.findById(session.id());
            if (entity == null) {
                entity = new VenueSessionEntity();
            }
        } else {
            entity = new VenueSessionEntity();
        }
        entity.setVenue(session.venue());
        entity.setDayOfWeek(session.dayOfWeek());
        entity.setPhase(session.phase());
        entity.setOpenTime(session.openTime());
        entity.setCloseTime(session.closeTime());
        entity.persist();
        return toDomain(entity);
    }

    @Override
    @Transactional
    public void delete(VenueSession session) {
        if (session.id() != null) {
            VenueSessionEntity.deleteById(session.id());
        }
    }

    private VenueSession toDomain(VenueSessionEntity entity) {
        return new VenueSession(
                entity.getId(),
                entity.getVenue(),
                entity.getDayOfWeek(),
                entity.getPhase(),
                entity.getOpenTime(),
                entity.getCloseTime()
        );
    }
}