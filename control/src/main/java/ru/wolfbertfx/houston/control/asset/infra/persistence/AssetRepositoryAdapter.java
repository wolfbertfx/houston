package ru.wolfbertfx.houston.control.asset.infra.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.common.asset.Segment;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.control.asset.domain.Asset;
import ru.wolfbertfx.houston.control.asset.domain.AssetRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AssetRepositoryAdapter implements AssetRepository, PanacheRepository<AssetEntity> {

    @Override
    public List<Asset> listAllAssets() {
        return listAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Asset> listByFilters(Status status, Venue venue, Segment segment) {
        PanacheQuery<AssetEntity> query;
        if (status != null) {query = find("status_id = ?1", status.getId());}
        else {query = findAll();}

        return query.list().stream().map(this::toDomain)
            .filter(a -> venue == null || a.ticker().getVenue().equals(venue))
            .filter(a -> segment == null || a.ticker().getSegment().equals(segment))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void upsert(Asset asset) {
        AssetEntity entity = find("ticker", asset.ticker()).firstResult();
        if (entity == null) {
            entity = new AssetEntity(); entity.setTicker(asset.ticker());
            entity.setStatus(asset.status()); persist(entity);
        } else {entity.setStatus(asset.status());}
    }

    private Asset toDomain(AssetEntity entity) {
        return new Asset(entity.getTicker(), entity.getStatus());
    }
}