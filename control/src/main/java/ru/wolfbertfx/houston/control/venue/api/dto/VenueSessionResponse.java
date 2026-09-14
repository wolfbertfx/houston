package ru.wolfbertfx.houston.control.venue.api.dto;

import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;

import java.time.LocalTime;

/** Ответ с информацией о базовой сессии площадки. */
public record VenueSessionResponse(
        long id,
        int venueId,
        String venueName,
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
    public int getDayOfWeek() { return dayOfWeek; }
    public int getPhaseId() { return phaseId; }
    public String getPhaseName() { return phaseName; }
    public LocalTime getOpenTime() { return openTime; }
    public LocalTime getCloseTime() { return closeTime; }
    public boolean isOvernight() { return overnight; }
}