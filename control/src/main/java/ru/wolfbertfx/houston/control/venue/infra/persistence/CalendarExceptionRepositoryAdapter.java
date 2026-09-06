package ru.wolfbertfx.houston.control.venue.infra.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.control.venue.domain.CalendarException;
import ru.wolfbertfx.houston.control.venue.domain.CalendarExceptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class CalendarExceptionRepositoryAdapter implements CalendarExceptionRepository {

    @Override
    public List<CalendarException> findByVenue(Venue venue) {
        return CalendarExceptionEntity.find("venue", venue).stream()
                .map(e -> toDomain((CalendarExceptionEntity) e))
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarException> findByVenueAndDateRange(Venue venue, LocalDate from, LocalDate to) {
        return CalendarExceptionEntity.find("venue = ?1 and startDate <= ?2 and endDate >= ?3", venue, to, from).stream()
                .map(e -> toDomain((CalendarExceptionEntity) e))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CalendarException> findById(Long id) {
        return CalendarExceptionEntity.findByIdOptional(id).map(e -> toDomain((CalendarExceptionEntity) e));
    }

    @Override
    @Transactional
    public CalendarException save(CalendarException exception) {
        CalendarExceptionEntity entity;
        if (exception.id() != null) {
            entity = CalendarExceptionEntity.findById(exception.id());
            if (entity == null) {
                entity = new CalendarExceptionEntity();
            }
        } else {
            entity = new CalendarExceptionEntity();
        }
        entity.setVenue(exception.venue());
        entity.setStartDate(exception.startDate());
        entity.setEndDate(exception.endDate());
        entity.setInstrument(exception.instrument());
        entity.setInstrumentType(exception.instrumentType());
        entity.setType(exception.type());
        entity.setDescription(exception.description());
        entity.persist();
        return toDomain(entity);
    }

    @Override
    @Transactional
    public void delete(CalendarException exception) {
        if (exception.id() != null) {
            CalendarExceptionEntity.deleteById(exception.id());
        }
    }

    private CalendarException toDomain(CalendarExceptionEntity entity) {
        return new CalendarException(
                entity.getId(),
                entity.getVenue(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getInstrument(),
                entity.getInstrumentType(),
                entity.getType(),
                entity.getDescription()
        );
    }
}