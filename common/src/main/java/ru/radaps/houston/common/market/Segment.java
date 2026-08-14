package ru.radaps.houston.common.market;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Сегментация активов для тематической классификации и фильтрации. */
public enum Segment {

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

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Segment::getId, e -> e));}

    Segment(int id) {
        this.id = id;
    }
    public int getId() {return id;}

    public static Segment fromId(int id) {
        var taxon = BY_ID.get(id);
        if (taxon == null) {throw new IllegalArgumentException("Unknown AssetTaxon ID: " + id);}
        return taxon;
    }
}
