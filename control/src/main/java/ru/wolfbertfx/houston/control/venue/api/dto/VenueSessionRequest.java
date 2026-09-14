package ru.wolfbertfx.houston.control.venue.api.dto;

import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;

import java.time.LocalTime;

/** Запрос создания/обновления базовой сессии площадки. */
public record VenueSessionRequest(
        int venueId,
        int dayOfWeek,
        int phaseId,
        LocalTime openTime,
        LocalTime closeTime
) {
    public int getVenueId() { return venueId; }
    public int getDayOfWeek() { return dayOfWeek; }
    public int getPhaseId() { return phaseId; }
    public LocalTime getOpenTime() { return openTime; }
    public LocalTime getCloseTime() { return closeTime; }
}