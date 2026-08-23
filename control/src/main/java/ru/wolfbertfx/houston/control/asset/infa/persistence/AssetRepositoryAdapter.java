package ru.wolfbertfx.houston.control.asset.infa.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import ru.wolfbertfx.houston.control.asset.domain.Asset;
import ru.wolfbertfx.houston.control.asset.domain.AssetRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AssetRepositoryAdapter implements AssetRepository, PanacheRepository<AssetEntity> {

    @Override
    public List<Asset> listAllAssets() {
        return listAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void upsert(Asset asset) {
        AssetEntity entity = find("instrument", asset.instrument()).firstResult();
        if (entity == null) {
            entity = new AssetEntity();
            entity.setInstrument(asset.instrument());
            entity.setStatus(asset.status());
            entity.setLastUpdated(asset.lastUpdated());
            persist(entity);
        } else {
            entity.setStatus(asset.status());
            entity.setLastUpdated(asset.lastUpdated());
        }
    }

    private Asset toDomain(AssetEntity entity) {
        return new Asset(entity.getInstrument(), entity.getStatus(), entity.getLastUpdated());
    }
}
