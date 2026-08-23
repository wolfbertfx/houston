package ru.wolfbertfx.houston.control.asset.infa.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.wolfbertfx.houston.common.asset.Instrument;

@Converter(autoApply = true)
class InstrumentConverter implements AttributeConverter<Instrument, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Instrument instrument) {
        return (instrument != null) ? instrument.getId() : null;
    }

    @Override
    public Instrument convertToEntityAttribute(Integer id) {
        return Instrument.fromId(id);
    }
}