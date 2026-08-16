package ru.wolfbertfx.houston.common.exchange;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Currency {

    /** 🇺🇸 Доллар США. */
    USD(1, "$"),
    /** 🇷🇺 Российский рубль. */
    RUB(2, "₽"),

    //ЕВРОПА
    /** 🇪🇺 Евро. */
    EUR(3, "€"),
    /** 🇬🇪 Грузинский лари. */
    GEL(4, "₾"),
    /** 🇬🇧 Британский фунт стерлингов. */
    GBP(5, "£"),
    /** 🇨🇭 Швейцарский франк. */
    CHF(6, "Fr"),

    //АЗИЯ
    /** 🇨🇳 Китайский юань. */
    CNY(7, "¥"),
    /** 🇰🇿 Казахстанский тенге. */
    KZT(8, "₸"),
    /** 🇯🇵 Японская иена. */
    JPY(9, "¥"),
    /** 🇰🇷 Южнокорейская вона. */
    KRW(10, "₩");

    private final int id;
    private final String symbol;

    private static final Map<Integer, Currency> BY_ISO;

    static {BY_ISO = Arrays.stream(values()).collect(Collectors.toMap(Currency::getId, e -> e));}

    Currency(int id, String symbol) {this.id = id; this.symbol = symbol;}

    public String getSymbol() { return symbol; }
    public int getId() {return id;}

    public static Currency fromId(int isoCode) {
        var currency = BY_ISO.get(isoCode);
        if (currency == null) {throw new IllegalArgumentException("Unknown Currency ISO code: " + isoCode);}
        return currency;
    }
}