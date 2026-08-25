package ru.wolfbertfx.houston.common.pipeline;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Правило переключения контрактов при склейке непрерывной серии (Pipeline.PANAMA).
 * Контракт для control (политика: какой метод + параметры) и processing (исполнение на реальных данных).
 * Назначение «инструмент → метод» живёт в политике control, а не здесь: типы методов — код, выбор — настройка.
 */
public enum Roll {

    /** Детерминированно: переключение за N дней до экспирации фронта. */
    BY_DATE(1),
    /** По объёму: бэк-месяц перерос фронт (объём бэк > threshold × объём фронта). */
    VOLUME(2),
    /** По открытому интересу: аналогично объёму, но по OI. */
    OPEN_INTEREST(3);

    private final int id;
    private static final Map<Integer, Roll> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Roll::getId, e -> e));}

    Roll(int id) {this.id = id;}
    public int getId() {return id;}

    public static Roll fromId(int id) {
        var method = BY_ID.get(id);
        if (method == null) throw new IllegalArgumentException("Unknown Roll ID: " + id);
        return method;
    }
}