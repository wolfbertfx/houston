package ru.wolfbertfx.houston.control.asset.domain;

import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Status;

/**
 * Порт кэша состояния активов.
 * Служит для быстрой отдачи статусов другим модулям (ingestion, guidance, console).
 */
public interface AssetCache {

    /** Сохранить статус инструмента в кэш. */
    void put(Instrument instrument, Status status);

    /** Проверить, есть ли флаг рассинхрона кэша. */
    default boolean isCacheOutOfSync() {
        return false;
    }

    /** Сбросить флаг рассинхрона после успешного прогрева. */
    default void markCacheSynced() {}
}
