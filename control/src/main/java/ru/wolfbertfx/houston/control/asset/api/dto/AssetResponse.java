package ru.wolfbertfx.houston.control.asset.api.dto;

import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Currency;
import ru.wolfbertfx.houston.common.asset.Discovery;
import ru.wolfbertfx.houston.common.asset.Nature;
import ru.wolfbertfx.houston.common.asset.Pipeline;
import ru.wolfbertfx.houston.common.asset.Segment;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.common.asset.Type;
import ru.wolfbertfx.houston.common.venue.Venue;

import java.time.Instant;

/**
 * Контракт представления актива для внешних потребителей (console).
 * Обогащён статическими метаданными инструмента, чтобы фронт не собирал их по частям.
 */
public record AssetResponse(
        int instrumentId,
        String symbol,
        Status status,
        Venue venue,
        Type type,
        Nature nature,
        Segment segment,
        Currency currency,
        Discovery discovery,
        Pipeline pipeline,
        Instant lastUpdated
) {
}