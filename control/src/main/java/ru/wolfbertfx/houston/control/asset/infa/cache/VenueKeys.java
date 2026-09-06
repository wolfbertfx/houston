package ru.wolfbertfx.houston.control.asset.infa.cache;

import ru.wolfbertfx.houston.common.asset.Venue;
import ru.wolfbertfx.houston.common.keys.Redis;

/**
 * Ключи Redis для конкретной площадки.
 * Пакет-приватный — используется внутри infra.cache.
 */
final class VenueKeys {

    private final String enabled;
    private final String preparing;

    private VenueKeys(String enabled, String preparing) {
        this.enabled = enabled;
        this.preparing = preparing;
    }

    String enabled() {
        return enabled;
    }

    String preparing() {
        return preparing;
    }

    static VenueKeys forVenue(Venue venue) {
        return new VenueKeys(Redis.venueAssetsEnabled(venue), Redis.venueAssetsPreparing(venue));
    }
}