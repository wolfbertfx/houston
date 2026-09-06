package ru.wolfbertfx.houston.control.venue.infra.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.wolfbertfx.houston.common.asset.Type;

@Converter(autoApply = true)
public class TypeConverter implements AttributeConverter<Type, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Type attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public Type convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Type.fromId(dbData);
    }
}