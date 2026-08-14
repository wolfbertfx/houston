package ru.radaps.houston.common.market;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Фундаментальная природа торгового актива. Определяет рыночную нишу и характеристики поведения. */
public enum Nature {

    /** Долевые инструменты (Акции, БПИФы на акции). */
    EQUITY(1),
    /** Валютные пары. */
    CURRENCY(2),
    /** Сырьевые товары (Нефть, Золото). */
    COMMODITY(3),
    /** Рыночные индексы. */
    INDEX(4),
    /** Цифровые активы. */
    CRYPTO(5);

    private final int id;
    private static final Map<Integer, Nature> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Nature::getId, e -> e));}

    Nature(int id) {this.id = id;}
    public int getId() {return id;}

    public static Nature fromId(int id) {
        var nature = BY_ID.get(id);
        if (nature == null) throw new IllegalArgumentException("Unknown Nature ID: " + id);
        return nature;
    }
}
