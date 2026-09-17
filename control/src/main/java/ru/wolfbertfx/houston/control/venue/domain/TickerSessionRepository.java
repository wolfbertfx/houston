package ru.wolfbertfx.houston.control.venue.domain;

import ru.wolfbertfx.houston.common.asset.Ticker;
import ru.wolfbertfx.houston.common.venue.Venue;

import java.util.List;

/**
 * Порт для работы с переопределениями сессий инструментов.
 */
public interface InstrumentSessionRepository {
    List<InstrumentSession> findByVenueAndDay(Venue venue, int dayOfWeek);
    List<InstrumentSession> findByVenue(Venue venue);
    InstrumentSession save(InstrumentSession session);
    void delete(InstrumentSession session);
}