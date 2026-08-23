package ru.wolfbertfx.houston.control.asset.domain;

import ru.wolfbertfx.houston.common.asset.Catalog;
import ru.wolfbertfx.houston.common.asset.Status;

import java.time.Instant;

/** Доменная модель актива. Является immutable-представлением конфигурации и состояния актива в системе. */
public record Asset(Catalog ticker, Status status, Instant lastUpdated) {
    public Asset withStatus(Status newStatus) {
        return new Asset(ticker, newStatus, Instant.now());
    }
}