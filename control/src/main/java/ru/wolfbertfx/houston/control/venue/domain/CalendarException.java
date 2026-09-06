package ru.wolfbertfx.houston.control.venue.domain;

import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Type;
import ru.wolfbertfx.houston.common.venue.Venue;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Исключение из календаря: праздники, спец-закрытия, сокращённые дни.
 * Применяется поверх VenueSession / InstrumentSession.
 */
public record CalendarException(
        Long id,
        Venue venue,
        LocalDate startDate,
        LocalDate endDate,
        Instrument instrument,           // null = все инструменты площадки
        Type instrumentType,             // null = все типы
        ExceptionType type,              // FULL_CLOSE / REDUCED_HOURS / CUSTOM_SCHEDULE
        String description
) {
    /** Покрывает ли исключение данную дату */
    public boolean covers(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarException that)) return false;
        return Objects.equals(venue, that.venue)
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Objects.equals(instrument, that.instrument)
                && Objects.equals(instrumentType, that.instrumentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(venue, startDate, endDate, instrument, instrumentType);
    }
}