package ru.wolfbertfx.houston.control.shared.infra.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.wolfbertfx.houston.common.asset.Ticker;

@Converter(autoApply = true)
public class TickerConverter implements AttributeConverter<Ticker, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Ticker ticker) {
        return ticker == null ? null : ticker.getId();
    }

    @Override
    public Ticker convertToEntityAttribute(Integer id) {
        return id == null ? null : Ticker.fromId(id);
    }
}