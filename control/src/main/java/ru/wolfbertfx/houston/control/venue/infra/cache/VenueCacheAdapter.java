package ru.wolfbertfx.houston.control.venue.infra.cache;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.set.SetCommands;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.venue.Phase;
import ru.wolfbertfx.houston.common.keys.Redis;
import ru.wolfbertfx.houston.control.venue.domain.VenueCache;

import java.util.Set;

@ApplicationScoped
public class VenueCacheAdapter implements VenueCache {

    private static final Logger log = LoggerFactory.getLogger(VenueCacheAdapter.class);

    private final SetCommands<String, String> setCommands;
    private final KeyCommands<String> keyCommands;

    public VenueCacheAdapter(RedisDataSource redisDataSource) {
        this.setCommands = redisDataSource.set(String.class, String.class);
        this.keyCommands = redisDataSource.key();
    }

    // --- LIVE set (OPEN phase) ---

    public void addToLive(Venue venue, Instrument instrument) {
        String key = Redis.venueAssetsLive(venue);
        String id = String.valueOf(instrument.getId());
        setCommands.sadd(key, String.valueOf(instrument.getId()));
        log.trace("Added to LIVE: {} -> {}", key, instrument.getId());
    }

    public void removeFromLive(Venue venue, Instrument instrument) {
        String key = Redis.venueAssetsLive(venue);
        setCommands.srem(key, String.valueOf(instrument.getId()));
    }

    public boolean isLive(Venue venue, Instrument instrument) {
        String key = Redis.venueAssetsLive(venue);
        return setCommands.sismember(key, String.valueOf(instrument.getId()));
    }

    public Set<String> getLive(Venue venue) {
        return setCommands.smembers(Redis.venueAssetsLive(venue));
    }

    public long liveCount(Venue venue) {
        return setCommands.scard(Redis.venueAssetsLive(venue));
    }

    // --- PREPARING set (PRE_MARKET phase) ---

    public void addToPreparing(Venue venue, Instrument instrument) {
        String key = Redis.venueAssetsPreparing(venue);
        setCommands.sadd(key, String.valueOf(instrument.getId()));
    }

    public void removeFromPreparing(Venue venue, Instrument instrument) {
        String key = Redis.venueAssetsPreparing(venue);
        setCommands.srem(key, String.valueOf(instrument.getId()));
    }

    public boolean isPreparing(Venue venue, Instrument instrument) {
        String key = Redis.venueAssetsPreparing(venue);
        return setCommands.sismember(key, String.valueOf(instrument.getId()));
    }

    public Set<String> getPreparing(Venue venue) {
        return setCommands.smembers(Redis.venueAssetsPreparing(venue));
    }

    public long preparingCount(Venue venue) {
        return setCommands.scard(Redis.venueAssetsPreparing(venue));
    }

    // --- ENABLED set (managed by AssetService, read-only here) ---

    public boolean isEnabled(Venue venue, Instrument instrument) {
        String key = "venue:" + venue.name().toLowerCase() + ":assets:enabled";
        return setCommands.sismember(key, String.valueOf(instrument.getId()));
    }

    // --- Utility ---

    public void clearVenue(Venue venue) {
        String liveKey = "venue:" + venue.name().toLowerCase() + ":assets:live";
        String preparingKey = "venue:" + venue.name().toLowerCase() + ":assets:preparing";
        keyCommands.del(liveKey, preparingKey);
    }
}