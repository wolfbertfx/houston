package ru.wolfbertfx.houston.control.venue.api.dto;

import ru.wolfbertfx.houston.common.venue.Phase;

import java.time.LocalTime;

/** Запрос создания/обновления переопределения сессии инструмента/типа. */
public record InstrumentSessionRequest(
        int venueId,
        Integer instrumentId,
        Integer typeId,
        int dayOfWeek,
        int phaseId,
        LocalTime openTime,
        LocalTime closeTime
) {
    public int getVenueId() { return venueId; }
    public Integer getInstrumentId() { return instrumentId; }
    public Integer getTypeId() { return typeId; }
    public int getDayOfWeek() { return dayOfWeek; }
    public int getPhaseId() { return phaseId; }
    public LocalTime getOpenTime() { return openTime; }
    public LocalTime getCloseTime() { return closeTime; }
}