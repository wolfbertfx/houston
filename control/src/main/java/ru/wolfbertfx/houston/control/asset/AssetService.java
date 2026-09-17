package ru.wolfbertfx.houston.control.asset;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wolfbertfx.houston.common.asset.Ticker;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.common.asset.Segment;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.control.asset.domain.Asset;
import ru.wolfbertfx.houston.control.asset.domain.AssetCache;
import ru.wolfbertfx.houston.control.asset.domain.AssetNotFoundException;
import ru.wolfbertfx.houston.control.asset.domain.AssetRepository;

import java.util.List;

@ApplicationScoped
public class AssetService {

    private static final Logger log = LoggerFactory.getLogger(AssetService.class);

    private final AssetRepository repository;
    private final AssetCache cache;

    public AssetService(AssetRepository repository, AssetCache cache) {
        this.repository = repository; this.cache = cache;
    }

    void onStart(@Observes StartupEvent ev) {
        log.info("System startup: triggering asset synchronization...");
        syncWithRegistry(Ticker.values()); warmupCache();
    }

    /** Получить активы с опциональными фильтрами (null = все) */
    public List<Asset> getAssets(Status status, Venue venue, Segment segment) {
        return repository.listByFilters(status, venue, segment);
    }

    @Transactional
    public void syncWithRegistry(Ticker[] instruments) {
        var existingInstruments = repository.listAllAssets().stream().map(Asset::ticker).toList();

        for (Ticker instrument : instruments) {
            if (!existingInstruments.contains(instrument)) {
                var asset = new Asset(instrument, Status.DISABLED); repository.upsert(asset);
            }
        }
    }

    private void warmupCache() {
        log.info("Warming up asset status cache in Redis...");
        List<Asset> assets = repository.listAllAssets();
        for (Asset asset : assets) {cache.put(asset.ticker(), asset.status());}
        cache.markCacheSynced();
        log.info("Asset status cache warmup complete: {} assets", assets.size());
    }

    /** Рекунслиация: Если кэш отмечен как out-of-sync — запускает полный прогрев из БД. */
    @Scheduled(every = "60s", identity = "asset-cache-reconciliation")
    void reconcileCache() {
        if (cache.isCacheOutOfSync()) {
            log.warn("Cache out-of-sync detected, triggering reconciliation..."); warmupCache();
        }
    }

    @Transactional
    public Asset updateStatus(Ticker instrument, Status status) {
        var existing = repository.listAllAssets().stream()
        .filter(a -> a.ticker().equals(instrument)).findFirst()
        .orElseThrow(() -> new AssetNotFoundException(instrument));
        
        var updated = existing.withStatus(status);
        repository.upsert(updated); cache.put(instrument, status);
        return updated;
    }
}