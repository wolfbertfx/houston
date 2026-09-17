package ru.wolfbertfx.houston.control.venue.infra.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.control.venue.domain.InstrumentSession;
import ru.wolfbertfx.houston.control.venue.domain.InstrumentSessionRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class InstrumentSessionRepositoryAdapter implements InstrumentSessionRepository {

    @Override
    public List<InstrumentSession> findByVenueAndDay(Venue venue, int dayOfWeek) {
        return InstrumentSessionEntity.find("venue = ?1 and dayOfWeek = ?2", venue, dayOfWeek).stream()
                .map(e -> toDomain((InstrumentSessionEntity) e))
                .collect(Collectors.toList());
    }

    @Override
    public List<InstrumentSession> findByVenue(Venue venue) {
        return InstrumentSessionEntity.find("venue", venue).stream()
                .map(e -> toDomain((InstrumentSessionEntity) e))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InstrumentSession save(InstrumentSession session) {
        InstrumentSessionEntity entity;
        if (session.id() != null) {
            entity = InstrumentSessionEntity.findById(session.id());
            if (entity == null) {
                entity = new InstrumentSessionEntity();
            }
        } else {
            entity = new InstrumentSessionEntity();
        }
        entity.setVenue(session.venue());
        entity.setTicker(session.instrument());
        entity.setForm(session.form());
        entity.setDayOfWeek(session.dayOfWeek());
        entity.setPhase(session.phase());
        entity.setOpenTime(session.openTime());
        entity.setCloseTime(session.closeTime());
        entity.persist();
        return toDomain(entity);
    }

    @Override
    @Transactional
    public void delete(InstrumentSession session) {
        if (session.id() != null) {
            InstrumentSessionEntity.deleteById(session.id());
        }
    }

    private InstrumentSession toDomain(InstrumentSessionEntity entity) {
        return new InstrumentSession(
                entity.getId(),
                entity.getVenue(),
                entity.getTicker(),
                entity.getForm(),
                entity.getDayOfWeek(),
                entity.getPhase(),
                entity.getOpenTime(),
                entity.getCloseTime()
        );
    }
}