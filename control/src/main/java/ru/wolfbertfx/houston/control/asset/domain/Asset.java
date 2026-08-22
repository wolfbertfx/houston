package ru.wolfbertfx.houston.control.asset.domain;

import ru.wolfbertfx.houston.common.asset.Mode;
import ru.wolfbertfx.houston.common.asset.Ticker;

import java.time.Instant;

/** Доменная модель актива. Является immutable-представлением конфигурации и состояния актива в системе. */
public record Asset(Ticker ticker, Mode mode, Instant lastUpdated) {
    public Asset withMode(Mode newMode) {
        return new Asset(ticker, newMode, Instant.now());
    }
}