package ru.wolfbertfx.houston.control.asset.infra.cache;

import ru.wolfbertfx.houston.common.asset.Venue;
import ru.wolfbertfx.houston.common.keys.Redis;

/**
 * Ключи Redis для статусов активов на конкретной площадке.
 * Пакет-приватный — используется внутри infra.cache.
 */
final class AssetStatusKeys {

    private final String enabled;
    private final String preparing;

    private AssetStatusKeys(String enabled, String preparing) {
        this.enabled = enabled;
        this.preparing = preparing;
    }

    String enabled() {
        return enabled;
    }

    String preparing() {
        return preparing;
    }

    static AssetStatusKeys forVenue(Venue venue) {
        return new AssetStatusKeys(
                Redis.venueAssetsEnabled(venue),
                Redis.venueAssetsPreparing(venue)
        );
    }
}