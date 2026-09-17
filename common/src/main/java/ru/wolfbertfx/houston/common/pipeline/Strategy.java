package ru.wolfbertfx.houston.common.pipeline;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Определяет стратегию обработки и нормализации потока рыночных данных. */
public enum Strategy {

    /** Прямая трансляция без обработки (для спотовых активов). */
    DIRECT(1),
    /** Склейка контрактов с переключением по методу PANAMA. */
    PANAMA(2),
    /** Обработка данных для бессрочных контрактов с учетом Funding Rate. */
    PERPETUAL(3);

    private final int id;
    private static final Map<Integer, Strategy> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Strategy::getId, e -> e));}

    Strategy(int id) {this.id = id;}
    public int getId() {return id;}

    public static Strategy fromId(int id) {
        var pipeline = BY_ID.get(id);
        if (pipeline == null) {throw new IllegalArgumentException("Unknown Strategy ID: " + id);}
        return pipeline;
    }
}