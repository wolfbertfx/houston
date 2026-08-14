/* Copyright (c) 2026 LLC "Radaps". All rights reserved. Internal Use Only. Confidential.*/
package com.radaps.ewaspace.houston.shared.market;

import com.radaps.ewaspace.houston.shared.Identifiable;
import com.radaps.ewaspace.houston.shared.Localizable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Фундаментальная природа торгового актива. Определяет рыночную нишу и характеристики поведения. */
public enum Nature implements Localizable, Identifiable {

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
    private static final Map<String, Nature> BY_NAME;

    static {
        BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Nature::getId, e -> e));
        BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(e -> e.name().toUpperCase(), e -> e));
    }

    Nature(int id) {this.id = id;}

    public int getId() {return id;}

    public static Nature fromId(int id) {
        var nature = BY_ID.get(id);
        if (nature == null) throw new IllegalArgumentException("Unknown Nature ID: " + id);
        return nature;
    }

    public static Nature fromString(String name) {
        var nature = BY_NAME.get(name.toUpperCase());
        if (nature == null) throw new IllegalArgumentException("Unknown Nature name: " + name);
        return nature;
    }

    @Override
    public String getLocaleKey() {
        return "market.nature." + this.name().toLowerCase();
    }
}
