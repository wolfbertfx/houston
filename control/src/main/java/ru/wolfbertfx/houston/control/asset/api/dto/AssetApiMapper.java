package ru.wolfbertfx.houston.control.asset.api.dto;

import ru.wolfbertfx.houston.control.asset.domain.Asset;

import java.util.List;

/**
 * Граница между доменом и API: домен ничего не знает о контракте REST,
 * API не пробрасывает доменные модели наружу.
 */
public final class AssetApiMapper {

    private AssetApiMapper() {
    }

    public static AssetResponse toResponse(Asset asset) {
        var instrument = asset.instrument();
        return new AssetResponse(
                instrument.getId(),
                instrument.getSymbol(),
                asset.status(),
                instrument.getVenue(),
                instrument.getType(),
                instrument.getNature(),
                instrument.getSegment(),
                instrument.getCurrency(),
                instrument.getPipeline(),
                asset.lastUpdated()
        );
    }

    public static List<AssetResponse> toResponses(List<Asset> assets) {
        return assets.stream().map(AssetApiMapper::toResponse).toList();
    }
}
