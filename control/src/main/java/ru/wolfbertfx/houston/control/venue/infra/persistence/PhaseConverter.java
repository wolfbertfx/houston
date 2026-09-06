package ru.wolfbertfx.houston.control.venue.infra.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.wolfbertfx.houston.common.venue.Phase;

@Converter(autoApply = true)
public class PhaseConverter implements AttributeConverter<Phase, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Phase attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public Phase convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Phase.fromId(dbData);
    }
}