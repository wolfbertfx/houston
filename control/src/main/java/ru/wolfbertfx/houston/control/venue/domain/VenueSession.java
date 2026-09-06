package ru.wolfbertfx.houston.control.venue.domain;

import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Базовая торговая сессия площадки для дня недели.
 * Определяет стандартное расписание: MOEX 10:00-18:45, CME 17:00-16:00+1d и т.д.
 */
public record VenueSession(
        Long id,
        Venue venue,
        int dayOfWeek,           // 1=MON .. 7=SUN (ISO-8601)
        Phase phase,             // PRE_MARKET / OPEN / POST_MARKET / CLOSED
        LocalTime openTime,
        LocalTime closeTime      // может быть > 24:00 (на след. день), тогда closeTime > openTime
) {
    /** Сессия переносится на следующий день (closeTime > openTime или closeTime < openTime но phase=OPEN и venue=фьючерсы) */
    public boolean isOvernight() {
        return closeTime.isBefore(openTime) || closeTime.equals(openTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VenueSession that)) return false;
        return venue == that.venue && dayOfWeek == that.dayOfWeek && phase == that.phase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(venue, dayOfWeek, phase);
    }
}