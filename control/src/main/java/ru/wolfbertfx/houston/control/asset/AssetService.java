package ru.wolfbertfx.houston.control.asset;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.control.asset.domain.Asset;
import ru.wolfbertfx.houston.control.asset.domain.AssetCache;
import ru.wolfbertfx.houston.control.asset.domain.AssetNotFoundException;
import ru.wolfbertfx.houston.control.asset.domain.AssetRepository;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class AssetService {

    private static final Logger log = LoggerFactory.getLogger(AssetService.class);

    private final AssetRepository assetRepository;
    private final AssetCache assetCache;

    public AssetService(AssetRepository assetRepository, AssetCache assetCache) {
        this.assetRepository = assetRepository;
        this.assetCache = assetCache;
    }

    void onStart(@Observes StartupEvent ev) {
        log.info("System startup: triggering asset synchronization...");
        syncWithRegistry(Instrument.values());
        warmupCache();
    }

    public List<Asset> getAllAssets() {
        return assetRepository.listAllAssets();
    }

    @Transactional
    public void syncWithRegistry(Instrument[] instruments) {
        var existingInstruments = assetRepository.listAllAssets().stream()
                .map(Asset::instrument)
                .toList();

        for (Instrument instrument : instruments) {
            if (!existingInstruments.contains(instrument)) {
                var asset = new Asset(instrument, Status.DISABLED, Instant.now());
                assetRepository.upsert(asset);
            }
        }
    }

    private void warmupCache() {
        log.info("Warming up asset status cache in Redis...");
        List<Asset> assets = assetRepository.listAllAssets();
        for (Asset asset : assets) {
            assetCache.put(asset.instrument(), asset.status());
        }
        assetCache.markCacheSynced();
        log.info("Asset status cache warmup complete: {} assets", assets.size());
    }

    /**
     * Фоновая задача рекунсилиации: каждые 60 секунд проверяет флаг рассинхрона.
     * Если кэш отмечен как out-of-sync — запускает полный прогрев из БД.
     */
    @Scheduled(every = "60s", identity = "asset-cache-reconciliation")
    void reconcileCache() {
        if (assetCache.isCacheOutOfSync()) {
            log.warn("Cache out-of-sync detected, triggering reconciliation...");
            warmupCache();
        }
    }

    @Transactional
    public Asset updateStatus(Instrument instrument, Status status) {
        var existing = assetRepository.listAllAssets().stream()
        .filter(a -> a.instrument().equals(instrument)).findFirst()
        .orElseThrow(() -> new AssetNotFoundException(instrument));
        
        var updated = existing.withStatus(status);
        assetRepository.upsert(updated); assetCache.put(instrument, status);
        return updated;
    }
}
