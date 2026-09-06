package ru.wolfbertfx.houston.common.keys;

import ru.wolfbertfx.houston.common.asset.Venue;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.common.pipeline.State;

/** Централизованное управление ключами Redis. */
public final class Redis {

    private Redis() {}

    /**
     * Set инструментов со статусом {@link Status#ENABLED}.
     * Качается + виден в console/guidance.
     * Формат: {@code venue:{venue_lowercase}:assets:enabled}
     */
    public static String venueAssetsEnabled(Venue venue) {
        return "venue:" + venue.name().toLowerCase() + ":assets:enabled";
    }

    /**
     * Set инструментов со статусом {@link Status#PREPARING}.
     * Качается, но СКРЫТ от console/guidance.
     * Формат: {@code venue:{venue_lowercase}:assets:preparing}
     */
    public static String venueAssetsPreparing(Venue venue) {
        return "venue:" + venue.name().toLowerCase() + ":assets:preparing";
    }

    /**
     * Set инструментов в состоянии {@link State#LIVE}.
     * Обновляется ingestion при переходе BACKFILL → LIVE.
     * Формат: {@code venue:{venue_lowercase}:assets:live}
     */
    public static String venueAssetsLive(Venue venue) {
        return "venue:" + venue.name().toLowerCase() + ":assets:live";
    }
}