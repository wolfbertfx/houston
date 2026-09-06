package ru.wolfbertfx.houston.control.shared.infra.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.wolfbertfx.houston.common.asset.Instrument;

@Converter(autoApply = true)
public class InstrumentConverter implements AttributeConverter<Instrument, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Instrument instrument) {
        return instrument == null ? null : instrument.getId();
    }

    @Override
    public Instrument convertToEntityAttribute(Integer id) {
        return id == null ? null : Instrument.fromId(id);
    }
}