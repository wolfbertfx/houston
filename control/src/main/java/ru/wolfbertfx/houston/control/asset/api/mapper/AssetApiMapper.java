package ru.wolfbertfx.houston.control.asset.api.mapper;

import ru.wolfbertfx.houston.control.asset.api.dto.AssetResponse;
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
        var ticker = asset.ticker();
        return new AssetResponse(
                ticker.getId(),
                ticker.getSymbol(),
                asset.status(),
                ticker.getVenue(),
                ticker.getForm(),
                ticker.getNature(),
                ticker.getSegment(),
                ticker.getCurrency(),
                ticker.getDiscovery(),
                ticker.getPipeline()
        );
    }

    public static List<AssetResponse> toResponses(List<Asset> assets) {
        return assets.stream().map(AssetApiMapper::toResponse).toList();
    }
}
