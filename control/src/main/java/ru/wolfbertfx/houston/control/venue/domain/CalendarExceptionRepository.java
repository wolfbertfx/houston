package ru.wolfbertfx.houston.control.venue.domain;

import ru.wolfbertfx.houston.common.venue.Venue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Порт для работы с исключениями календаря. */
public interface CalendarExceptionRepository {
    List<CalendarException> findByVenue(Venue venue);
    List<CalendarException> findByVenueAndDateRange(Venue venue, LocalDate from, LocalDate to);
    Optional<CalendarException> findById(Long id);
    CalendarException save(CalendarException exception);
    void delete(CalendarException exception);
}