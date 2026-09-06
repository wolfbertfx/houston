package ru.wolfbertfx.houston.control.venue.infra.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.wolfbertfx.houston.common.venue.Venue;

@Converter(autoApply = true)
public class VenueConverter implements AttributeConverter<Venue, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Venue attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public Venue convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Venue.fromId(dbData);
    }
}