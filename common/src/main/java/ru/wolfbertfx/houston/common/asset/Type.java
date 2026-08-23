package ru.wolfbertfx.houston.common.asset;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Тип актива: форма, в которой ряд существует на рынке (торгуемый контракт или расчётная публикация). */
public enum Type {

    /** Спотовая торговля (непосредственная поставка). */
    SPOT(1),
    /** Срочный контракт с датой экспирации. */
    FUTURES(2),
    /** Бессрочный контракт (механизм Funding Rate). */
    PERPETUAL(3),
    /** Биржевой инвестиционный фонд. */
    ETF(4),
    /** Опционный контракт. */
    OPTION(5),
    /** Расчётный (неторгуемый) ряд биржи: индексы и индикаторы. Нельзя купить — можно только читать. */
    CALC(6);

    private final int id;
    private static final Map<Integer, Type> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Type::getId, e -> e));}

    Type(int id) {this.id = id;}
    public int getId() {return id;}

    public static Type fromId(int id) {
        var type = BY_ID.get(id);
        if (type == null) throw new IllegalArgumentException("Unknown Type ID: " + id);
        return type;
    }
}