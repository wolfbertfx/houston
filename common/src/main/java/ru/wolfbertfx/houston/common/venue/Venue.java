package ru.wolfbertfx.houston.common.venue;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Представляет собой источник истины (Primary Exchange) */
public enum Venue {

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

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Venue::getId, e -> e));}

    Venue(int id, String timeZone, boolean hasOrderBook, boolean hasTradingSession, boolean hasCentralClearing) {
        this.id = id; this.timeZone = timeZone; this.hasOrderBook = hasOrderBook;
        this.hasTradingSession = hasTradingSession; this.hasCentralClearing = hasCentralClearing;
    }

    public int getId() {return id;}
    public String getTimeZone() { return timeZone; }
    public boolean hasOrderBook() { return hasOrderBook; }
    public boolean hasTradingSession() { return hasTradingSession; }
    public boolean hasCentralClearing() { return hasCentralClearing; }

    public static Venue fromId(int id) {
        var venue = BY_ID.get(id);
        if (venue == null) throw new IllegalArgumentException("Unknown Venue ID: " + id);
        return venue;
    }
}
