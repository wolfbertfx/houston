/* Copyright (c) 2026 LLC "Radaps". All rights reserved. Internal Use Only. Confidential.*/
package com.radaps.ewaspace.houston.shared.exchange;

import com.radaps.ewaspace.houston.shared.Identifiable;
import com.radaps.ewaspace.houston.shared.Localizable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Представляет собой источник истины (Primary Exchange) */
public enum Venue implements Localizable, Identifiable {

    /** Intercontinental Exchange (ICE) */
    ICE(1, "Europe/London", true, true, true),
    /** Moscow Exchange (MOEX) */
    MOEX(2, "Europe/Moscow", true, true, true),
    /** New York Mercantile Exchange (NYMEX) */
    NYMEX(3, "America/New_York", true, true, true),
    /** Commodity Exchange (COMEX) */
    COMEX(4, "America/New_York", true, true, true),
    /** Chicago Mercantile Exchange (CME) */
    CME(5, "America/Chicago", true, true, true),
    /** Chicago Board Options Exchange (CBOE) */
    CBOE(6, "America/Chicago", true, true, true),
    /** Global Forex Market (OTC) */
    FOREX(7, "UTC", false, true, false),
    /** Binance (Crypto Exchange) */
    BINANCE(8, "UTC", false, false, false);

    private final int id;
    private final String timeZone;
    private final boolean hasOrderBook;
    private final boolean hasTradingSession;
    private final boolean hasCentralClearing;

    private static final Map<Integer, Venue> BY_ID;
    private static final Map<String, Venue> BY_NAME;

    static {
        BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Venue::getId, e -> e));
        BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(e -> e.name().toUpperCase(), e -> e));
    }

    Venue(int id, String timeZone, boolean hasOrderBook, boolean hasTradingSession, boolean hasCentralClearing) {
        this.id = id;
        this.timeZone = timeZone;
        this.hasOrderBook = hasOrderBook;
        this.hasTradingSession = hasTradingSession;
        this.hasCentralClearing = hasCentralClearing;
    }

    public String getTimeZone() { return timeZone; }
    public boolean hasOrderBook() { return hasOrderBook; }
    public boolean hasTradingSession() { return hasTradingSession; }
    public boolean hasCentralClearing() { return hasCentralClearing; }

    public static Venue fromId(int id) {
        Venue venue = BY_ID.get(id);
        if (venue == null) throw new IllegalArgumentException("Unknown Venue ID: " + id);
        return venue;
    }

    public static Venue fromString(String name) {
        Venue venue = BY_NAME.get(name.toUpperCase());
        if (venue == null) throw new IllegalArgumentException("Unknown Venue name: " + name);
        return venue;
    }

    @Override
    public int getId() {return id;}

    @Override
    public String getLocaleKey() {
        return "exchange.venue." + this.name().toLowerCase();
    }
}
