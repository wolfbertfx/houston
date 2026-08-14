/* Copyright (c) 2026 LLC "Radaps". All rights reserved. Internal Use Only. Confidential.*/
package com.radaps.ewaspace.houston.shared.exchange;

import com.radaps.ewaspace.houston.shared.Identifiable;
import com.radaps.ewaspace.houston.shared.Localizable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Currency implements Localizable, Identifiable {

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

    private final int isoCode;
    private final String symbol;

    private static final Map<Integer, Currency> BY_ISO;
    private static final Map<String, Currency> BY_NAME;

    static {
        BY_ISO = Arrays.stream(values()).collect(Collectors.toMap(Currency::getId, e -> e));
        BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(e -> e.name().toUpperCase(), e -> e));
    }

    Currency(int id, String symbol) {
        this.isoCode = id; this.symbol = symbol;
    }

    public String getSymbol() { return symbol; }

    public static Currency fromId(int isoCode) {
        var currency = BY_ISO.get(isoCode);
        if (currency == null) {throw new IllegalArgumentException("Unknown Currency ISO code: " + isoCode);}
        return currency;
    }

    public static Currency fromString(String name) {
        var currency = BY_NAME.get(name.toUpperCase());
        if (currency == null) {throw new IllegalArgumentException("Unknown Currency name: " + name);}
        return currency;
    }

    @Override
    public String getLocaleKey() {
        return "exchange.currency." + this.name().toLowerCase();
    }

    @Override
    public int getId() {return isoCode;}
}
