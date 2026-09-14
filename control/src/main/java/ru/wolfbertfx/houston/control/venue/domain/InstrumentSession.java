package ru.wolfbertfx.houston.control.venue.domain;

import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.common.asset.Type;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.common.venue.Phase;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Переопределение торговой сессии для конкретного инструмента или типа инструмента
 * на конкретной площадке. Используется когда у инструмента (или типа) сессия отличается
 * от базовой сессии площадки (VenueSession).
 * <p>
 * Пример: фьючерсы на MOEX торгуются дольше спотов.
 * Либо {@code instrumentId} заполнен (конкретный инструмент), либо {@code typeId} (все инструменты типа).
 * Не оба одновременно.
 */
public record InstrumentSession(
        Long id,
        Venue venue,
        Instrument instrument,           // null = применить ко всем инструментам типа typeId
        Type type,                       // null = конкретный инструмент instrumentId
        int dayOfWeek,                   // 1=MON .. 7=SUN
        Phase phase,
        LocalTime openTime,
        LocalTime closeTime
) {
    /** Сессия переносится на следующий день */
    public boolean isOvernight() {
        return closeTime.isBefore(openTime) || closeTime.equals(openTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstrumentSession that)) return false;
        return venue == that.venue
                && Objects.equals(instrument, that.instrument)
                && Objects.equals(type, that.type)
                && dayOfWeek == that.dayOfWeek
                && phase == that.phase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(venue, instrument, type, dayOfWeek, phase);
    }

    /** Фабричный метод для конкретного инструмента */
    public static InstrumentSession forInstrument(Venue venue, Instrument instrument, int dayOfWeek, Phase phase, LocalTime open, LocalTime close) {
        return new InstrumentSession(null, venue, instrument, null, dayOfWeek, phase, open, close);
    }

    /** Фабричный метод для типа инструментов */
    public static InstrumentSession forType(Venue venue, Type type, int dayOfWeek, Phase phase, LocalTime open, LocalTime close) {
        return new InstrumentSession(null, venue, null, type, dayOfWeek, phase, open, close);
    }
}