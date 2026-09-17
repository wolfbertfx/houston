package ru.wolfbertfx.houston.common.asset;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Форма актива в которой ряд существует на рынке (торгуемый контракт или расчётная публикация). */
public enum Form {

    /** Спотовая торговля (непосредственная поставка). */
    SPOT(1),
    /** Склееный ряд фьючерсных контрактов. */
    CONTINUOUS(2),
    /** Бессрочный фьючерсный контракт (механизм Funding Rate). */
    PERPETUAL(3),
    /** Биржевой инвестиционный фонд. */
    ETF(4),
    /** Расчётный (неторгуемый) ряд биржи: индексы и индикаторы. Нельзя купить — можно только читать. */
    CALC(5);

    private final int id;
    private static final Map<Integer, Form> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Form::getId, e -> e));}

    Form(int id) {this.id = id;}
    public int getId() {return id;}

    public static Form fromId(int id) {
        var type = BY_ID.get(id);
        if (type == null) throw new IllegalArgumentException("Unknown Form ID: " + id);
        return type;
    }
}