package ru.wolfbertfx.houston.common.asset;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Валюта котирования актива.
 * Ведётся по внутренним числовым ID (как остальные справочники common), а не по кодам ISO 4217:
 * у криптовалют и ряда синтетических единиц ISO-кодов нет.
 * Символ — только для отображения в UI.
 */
public enum Currency {

    /** Доллар США. */
    USD(1, "$"),
    /** Российский рубль. */
    RUB(2, "₽"),

    //ЕВРОПА
    /** Евро. */
    EUR(3, "€"),
    /** Грузинский лари. */
    GEL(4, "₾"),
    /** Британский фунт стерлингов. */
    GBP(5, "£"),
    /** Швейцарский франк. */
    CHF(6, "Fr"),

    //АЗИЯ
    /** Китайский юань. */
    CNY(7, "¥"),
    /** Казахстанский тенге. */
    KZT(8, "₸"),
    /** Японская иена. */
    JPY(9, "¥"),
    /** Южнокорейская вона. */
    KRW(10, "₩");

    private final int id;
    private final String symbol;

    private static final Map<Integer, Currency> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Currency::getId, e -> e));}

    Currency(int id, String symbol) {this.id = id; this.symbol = symbol;}

    public String getSymbol() { return symbol; }
    public int getId() {return id;}

    public static Currency fromId(int id) {
        var currency = BY_ID.get(id);
        if (currency == null) {throw new IllegalArgumentException("Unknown Currency ID: " + id);}
        return currency;
    }
}