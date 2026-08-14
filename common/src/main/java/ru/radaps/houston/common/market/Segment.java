/* Copyright (c) 2026 LLC "Radaps". All rights reserved. Internal Use Only. Confidential.*/
package com.radaps.ewaspace.houston.shared.market;

import com.radaps.ewaspace.houston.shared.Identifiable;
import com.radaps.ewaspace.houston.shared.Localizable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Сегментация активов для тематической классификации и фильтрации. */
public enum Segment implements Localizable, Identifiable {

    /** Commodities. Нефть, газ; Золото, серебро; Пшеница, Кукуруза; Кофе, Сахар; */
    ENERGY(1), METALS(2), GRAINS(3), SOFT(4),
    /** Indices. S&P 500, IMOEX; RGBI, US10Y; DXY */
    EQUITY(5), BOND(6), CURRENCY(7),
    /** Stocks. Лукойл; Сбер; Норникель; X5; Яндекс; РусГидро; ПИК */
    OIL_GAS(8), FINANCE(9), MINING(10), RETAIL(11), TECH(12), UTILITIES(13), DEVELOPERS(14),
    /** Currency. USD, EUR, GBP, JPY; CNY, RUB, TRY; */
    MAJORS(15), REGIONAL(16), CROSSES(17), EXOTIC(18);

    private final int id;
    private static final Map<Integer, Segment> BY_ID;
    private static final Map<String, Segment> BY_NAME;

    static {
        BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Segment::getId, e -> e));
        BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(e -> e.name().toUpperCase(), e -> e));
    }

    Segment(int id) {
        this.id = id;
    }

    public static Segment fromId(int id) {
        Segment taxon = BY_ID.get(id);
        if (taxon == null) {
            throw new IllegalArgumentException("Unknown AssetTaxon ID: " + id);
        }
        return taxon;
    }

    public static Segment fromString(String name) {
        Segment taxon = BY_NAME.get(name.toUpperCase());
        if (taxon == null) {
            throw new IllegalArgumentException("Unknown AssetTaxon name: " + name);
        }
        return taxon;
    }

    @Override
    public int getId() {return id;}

    @Override
    public String getLocaleKey() {
        return "market.segment." + this.name().toLowerCase();
    }
}
