package ru.wolfbertfx.houston.control.asset.domain;

import java.util.List;
import ru.wolfbertfx.houston.common.asset.Segment;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.common.venue.Venue;

public interface AssetRepository {

    /** Все активы без фильтрации (для кэша/рекунсилиации). */
    List<Asset> listAllAssets();

    /** Фильтрация активов, null в любом параметре = "все значения". */
    List<Asset> listByFilters(Status status, Venue venue, Segment segment);

    /** Upsert актива при синхронизации реестра тикеров. */
    void upsert(Asset asset);
}