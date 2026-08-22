package ru.wolfbertfx.houston.common.asset;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Режим актива, выставляемый администратором (воля человека).
 * Определяет, разрешено ли системе работать с инструментом.
 * Не отражает текущую активность — для этого существует {@link Status}.
 */
public enum Mode {

    /** Актив в архиве. Система игнорирует любые операции с ним. */
    DISABLED(0),
    /** Актив готов к работе. Система сама решает, что с ним делать (докачка/живой сбор). */
    ENABLED(1),
    /** Ручная блокировка для проведения технических работ или корректировки данных. */
    MAINTENANCE(2);

    private final int id;
    private static final Map<Integer, Mode> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Mode::getId, e -> e));}

    Mode(int id) {this.id = id;}
    public int getId() {return id;}

    public static Mode fromId(int id) {
        var mode = BY_ID.get(id);
        if (mode == null) throw new IllegalArgumentException("Unknown Mode ID: " + id);
        return mode;
    }
}