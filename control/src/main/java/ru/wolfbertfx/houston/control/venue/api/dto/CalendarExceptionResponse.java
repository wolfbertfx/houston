package ru.wolfbertfx.houston.control.venue.api.dto;

import ru.wolfbertfx.houston.control.venue.domain.CalendarException;

import java.time.LocalDate;

/** Ответ с информацией об исключении календаря. */
public record CalendarExceptionResponse(
        long id,
        int venueId,
        String venueName,
        java.time.LocalDate startDate,
        java.time.LocalDate endDate,
        Integer instrumentId,
        String instrumentSymbol,
        Integer typeId,
        String typeName,
        CalendarException.ExceptionType type,
        String description
) {
    public long getId() { return id; }
    public int getVenueId() { return venueId; }
    public String getVenueName() { return venueName; }
    public java.time.LocalDate getStartDate() { return startDate; }
    public java.time.LocalDate getEndDate() { return endDate; }
    public Integer getInstrumentId() { return instrumentId; }
    public String getInstrumentSymbol() { return instrumentSymbol; }
    public Integer getTypeId() { return typeId; }
    public String getTypeName() { return typeName; }
    public CalendarException.ExceptionType getType() { return type; }
    public String getDescription() { return description; }
}