package ru.wolfbertfx.houston.control.venue.api.dto;

import java.time.LocalTime;

/** Ответ с информацией о переопределении сессии инструмента/типа. */
public record InstrumentSessionResponse(
        long id,
        int venueId,
        String venueName,
        Integer instrumentId,
        String instrumentSymbol,
        Integer typeId,
        String typeName,
        int dayOfWeek,
        int phaseId,
        String phaseName,
        LocalTime openTime,
        LocalTime closeTime,
        boolean overnight
) {
    public long getId() { return id; }
    public int getVenueId() { return venueId; }
    public String getVenueName() { return venueName; }
    public Integer getInstrumentId() { return instrumentId; }
    public String getInstrumentSymbol() { return instrumentSymbol; }
    public Integer getTypeId() { return typeId; }
    public String getTypeName() { return typeName; }
    public int getDayOfWeek() { return dayOfWeek; }
    public int getPhaseId() { return phaseId; }
    public String getPhaseName() { return phaseName; }
    public LocalTime getOpenTime() { return openTime; }
    public LocalTime getCloseTime() { return closeTime; }
    public boolean isOvernight() { return overnight; }
}