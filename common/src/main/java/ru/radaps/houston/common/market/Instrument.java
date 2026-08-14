package ru.radaps.houston.common.market;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Технический способ упаковки или исполнения актива. */
public enum Instrument {

    /** Спотовая торговля (непосредственная поставка). */
    SPOT(1),
    /** Срочный контракт с датой экспирации. */
    FUTURES(2),
    /** Бессрочный контракт (механизм Funding Rate). */
    PERPETUAL(3),
    /** Биржевой инвестиционный фонд. */
    ETF(4),
    /** Опционный контракт. */
    OPTION(5);

    private final int id;
    private static final Map<Integer, Instrument> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Instrument::getId, e -> e));}

    Instrument(int id) {this.id = id;}
    public int getId() {return id;}

    public static Instrument fromId(int id) {
        var instrument = BY_ID.get(id);
        if (instrument == null) throw new IllegalArgumentException("Unknown Instrument ID: " + id);
        return instrument;
    }
}
