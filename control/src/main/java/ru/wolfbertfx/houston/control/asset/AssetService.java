package ru.wolfbertfx.houston.control.asset;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wolfbertfx.houston.common.asset.Mode;
import ru.wolfbertfx.houston.common.asset.Ticker;
import ru.wolfbertfx.houston.control.asset.domain.Asset;
import ru.wolfbertfx.houston.control.asset.domain.AssetRepository;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class AssetService {

    private static final Logger log = LoggerFactory.getLogger(AssetService.class);

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    void onStart(@Observes StartupEvent ev) {
        log.info("System startup: triggering asset synchronization...");
        syncWithEnum(Ticker.values());
    }

    public List<Asset> getAllAssets() {
        return assetRepository.listAllAssets();
    }

    @Transactional
    public void syncWithEnum(Ticker[] enumTickers) {
        var existingTickers = assetRepository.listAllAssets().stream()
                .map(Asset::ticker)
                .toList();

        for (Ticker ticker : enumTickers) {
            if (!existingTickers.contains(ticker)) {
                Asset asset = new Asset(ticker, Mode.DISABLED, Instant.now());
                assetRepository.upsert(asset);
            }
        }
    }

    @Transactional
    public Asset updateMode(Ticker ticker, Mode mode) {
        var existing = assetRepository.listAllAssets().stream()
                .filter(a -> a.ticker().equals(ticker))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + ticker));
        
        Asset updated = existing.withMode(mode);
        assetRepository.upsert(updated);
        return updated;
    }
}
