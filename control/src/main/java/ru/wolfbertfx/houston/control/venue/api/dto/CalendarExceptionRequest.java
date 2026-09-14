package ru.wolfbertfx.houston.control.venue.api.dto;

import ru.wolfbertfx.houston.control.venue.domain.CalendarException;

import java.time.LocalDate;

/** Запрос создания/обновления исключения календаря. */
public record CalendarExceptionRequest(
        int venueId,
        LocalDate startDate,
        LocalDate endDate,
        Integer instrumentId,
        Integer typeId,
        CalendarException.ExceptionType type,
        String description
) {
    public int getVenueId() { return venueId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Integer getInstrumentId() { return instrumentId; }
    public Integer getTypeId() { return typeId; }
    public CalendarException.ExceptionType getType() { return type; }
    public String getDescription() { return description; }
}