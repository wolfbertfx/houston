package ru.wolfbertfx.houston.control.asset.domain;

import java.util.List;

public interface AssetRepository {
    List<Asset> listAllAssets();
    void upsert(Asset asset);
}
