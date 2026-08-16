package ru.wolfbertfx.houston.control.asset.infa.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.wolfbertfx.houston.common.asset.Ticker;

@Converter(autoApply = true)
class TickerConverter implements AttributeConverter<Ticker, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Ticker ticker) {
        return (ticker != null) ? ticker.getId() : null;
    }

    @Override
    public Ticker convertToEntityAttribute(Integer id) {
        return Ticker.fromId(id);
    }
}
