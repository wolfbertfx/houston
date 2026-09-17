package ru.wolfbertfx.houston.control.asset.domain;

import ru.wolfbertfx.houston.common.asset.Ticker;
import ru.wolfbertfx.houston.common.asset.Status;

/** Доменная модель актива. Является immutable-представлением конфигурации и состояния актива в системе. */
public record Asset(Ticker ticker, Status status) {
    public Asset withStatus(Status newStatus) {
        return new Asset(ticker, newStatus);
    }
}