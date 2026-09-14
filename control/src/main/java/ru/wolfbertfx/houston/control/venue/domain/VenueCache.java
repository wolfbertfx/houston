package ru.wolfbertfx.houston.control.venue.domain;

import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;

import java.util.Set;

/**
 * Порт кэша торговых сессий площадок.
 * Предоставляет операции для управления множествами инструментов в Redis:
 * LIVE (фаза OPEN), PREPARING (фаза PRE_MARKET), ENABLED.
 */
public interface VenueCache {

    // --- LIVE set (фаза OPEN) ---

    /** Добавить инструмент в LIVE множество площадки. */
    void addToLive(Venue venue, Instrument instrument);

    /** Удалить инструмент из LIVE множества площадки. */
    void removeFromLive(Venue venue, Instrument instrument);

    /** Проверить, находится ли инструмент в LIVE множестве. */
    boolean isLive(Venue venue, Instrument instrument);

    /** Получить все инструменты в LIVE множестве площадки. */
    Set<String> getLive(Venue venue);

    /** Количество инструментов в LIVE множестве. */
    long liveCount(Venue venue);

    // --- PREPARING set (фаза PRE_MARKET) ---

    /** Добавить инструмент в PREPARING множество площадки. */
    void addToPreparing(Venue venue, Instrument instrument);

    /** Удалить инструмент из PREPARING множества. */
    void removeFromPreparing(Venue venue, Instrument instrument);

    /** Проверить, находится ли инструмент в PREPARING множестве. */
    boolean isPreparing(Venue venue, Instrument instrument);

    /** Получить все инструменты в PREPARING множестве. */
    Set<String> getPreparing(Venue venue);

    /** Количество инструментов в PREPARING множестве. */
    long preparingCount(Venue venue);

    // --- ENABLED set (только чтение, управление в AssetService) ---

    /** Проверить, находится ли инструмент в ENABLED множестве. */
    boolean isEnabled(Venue venue, Instrument instrument);

    // --- Утилиты ---

    /** Очистить все множества площадки (LIVE, PREPARING). */
    void clearVenue(Venue venue);
}