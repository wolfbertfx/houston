package ru.wolfbertfx.houston.control.asset;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.control.asset.domain.Asset;
import ru.wolfbertfx.houston.control.asset.domain.AssetNotFoundException;
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
        syncWithRegistry(Instrument.values());
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

    @Transactional
    public Asset updateStatus(Instrument instrument, Status status) {
        var existing = assetRepository.listAllAssets().stream()
                .filter(a -> a.instrument().equals(instrument))
                .findFirst()
                .orElseThrow(() -> new AssetNotFoundException(instrument));
        
        Asset updated = existing.withStatus(status);
        assetRepository.upsert(updated);
        return updated;
    }
}
