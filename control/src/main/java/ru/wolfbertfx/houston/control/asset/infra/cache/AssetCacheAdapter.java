package ru.wolfbertfx.houston.control.asset.infra.cache;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.set.SetCommands;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.control.asset.domain.AssetCache;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class AssetCacheAdapter implements AssetCache {

    private static final Logger log = LoggerFactory.getLogger(AssetCacheAdapter.class);

    private final SetCommands<String, String> setCommands;
    private final AtomicBoolean cacheOutOfSync = new AtomicBoolean(false);

    public AssetCacheAdapter(RedisDataSource redisDataSource) {
        this.setCommands = redisDataSource.set(String.class, String.class);
    }

    @Override
    @Retry(delay = 100, delayUnit = ChronoUnit.MILLIS)
    @Fallback(fallbackMethod = "fallbackPut")
    public void put(Instrument instrument, Status status) {
        var keys = AssetStatusKeys.forVenue(instrument.getVenue());
        var id = String.valueOf(instrument.getId());

        switch (status) {
            case ENABLED -> {
                setCommands.sadd(keys.enabled(), id);
                setCommands.srem(keys.preparing(), id);
            }
            case PREPARING -> {
                setCommands.sadd(keys.preparing(), id);
                setCommands.srem(keys.enabled(), id);
            }
            default -> { // DISABLED
                setCommands.srem(keys.enabled(), id);
                setCommands.srem(keys.preparing(), id);
            }
        }
    }

    public void fallbackPut(Instrument instrument, Throwable t) {
        markOutOfSync(instrument.getVenue(), t);
    }

    private void markOutOfSync(Venue venue, Throwable t) {
        cacheOutOfSync.set(true);
        log.warn("Redis operation failed for venue [{}]: {}. Cache marked out-of-sync.", venue, t.getMessage());
    }

    public boolean isCacheOutOfSync() {
        return cacheOutOfSync.get();
    }

    public void markCacheSynced() {
        if (cacheOutOfSync.compareAndSet(true, false)) {
            log.info("Redis cache re-synced successfully");
        }
    }
}