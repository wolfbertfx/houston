/* Copyright (c) 2026 LLC "Radaps". All rights reserved. Internal Use Only. Confidential.*/
package com.radaps.ewaspace.houston.shared.asset;

import com.radaps.ewaspace.houston.shared.Identifiable;
import com.radaps.ewaspace.houston.shared.Localizable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Определяет стратегию обработки и нормализации потока рыночных данных. */
public enum Pipeline implements Localizable, Identifiable {

    /** Прямая трансляция без обработки (для спотовых активов). */
    DIRECT(1),
    /** Склейка контрактов с переключением по методу PANAMA. */
    PANAMA(2),
    /** Обработка данных для бессрочных контрактов с учетом Funding Rate. */
    PERPETUAL(3);

    private final int id;
    private static final Map<Integer, Pipeline> BY_ID;
    private static final Map<String, Pipeline> BY_NAME;

    static {
        BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Pipeline::getId, e -> e));
        BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(e -> e.name().toUpperCase(), e -> e));
    }

    Pipeline(int id) {this.id = id;}

    public static Pipeline fromId(int id) {
        var pipeline = BY_ID.get(id);
        if (pipeline == null) {throw new IllegalArgumentException("Unknown Pipeline ID: " + id);}
        return pipeline;
    }

    public static Pipeline fromString(String name) {
        var pipeline = BY_NAME.get(name.toUpperCase());
        if (pipeline == null) {throw new IllegalArgumentException("Unknown Pipeline name: " + name);}
        return pipeline;
    }

    @Override
    public int getId() {return id;}

    @Override
    public String getLocaleKey() {
        return "asset.pipeline." + this.name().toLowerCase();
    }
}
