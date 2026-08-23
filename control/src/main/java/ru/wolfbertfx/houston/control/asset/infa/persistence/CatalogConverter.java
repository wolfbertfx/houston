package ru.wolfbertfx.houston.control.asset.infa.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.wolfbertfx.houston.common.asset.Catalog;

@Converter(autoApply = true)
class CatalogConverter implements AttributeConverter<Catalog, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Catalog ticker) {
        return (ticker != null) ? ticker.getId() : null;
    }

    @Override
    public Catalog convertToEntityAttribute(Integer id) {
        return Catalog.fromId(id);
    }
}