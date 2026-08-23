package ru.wolfbertfx.houston.control.asset.domain;

import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Status;

import java.time.Instant;

/** Доменная модель актива. Является immutable-представлением конфигурации и состояния актива в системе. */
public record Asset(Instrument instrument, Status status, Instant lastUpdated) {
    public Asset withStatus(Status newStatus) {
        return new Asset(instrument, newStatus, Instant.now());
    }
}