/* Copyright (c) 2026 LLC "Radaps". All rights reserved. Internal Use Only. Confidential.*/
package com.radaps.ewaspace.houston.shared.market;

import com.radaps.ewaspace.houston.shared.Identifiable;
import com.radaps.ewaspace.houston.shared.Localizable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Технический способ упаковки или исполнения актива. */
public enum Instrument implements Localizable, Identifiable {

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
    private static final Map<String, Instrument> BY_NAME;

    static {
        BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Instrument::getId, e -> e));
        BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(e -> e.name().toUpperCase(), e -> e));
    }

    Instrument(int id) {this.id = id;}

    public static Instrument fromId(int id) {
        var instrument = BY_ID.get(id);
        if (instrument == null) throw new IllegalArgumentException("Unknown Instrument ID: " + id);
        return instrument;
    }

    public static Instrument fromString(String name) {
        var instrument = BY_NAME.get(name.toUpperCase());
        if (instrument == null) throw new IllegalArgumentException("Unknown Instrument name: " + name);
        return instrument;
    }

    @Override
    public int getId() {return id;}

    @Override
    public String getLocaleKey() {
        return "market.instrument." + this.name().toLowerCase();
    }
}
