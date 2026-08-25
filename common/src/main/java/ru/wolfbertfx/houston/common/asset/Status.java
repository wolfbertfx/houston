package ru.wolfbertfx.houston.common.asset;

import ru.wolfbertfx.houston.common.pipeline.State;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Жизненный цикл актива в системе, выставляемый администратором (воля человека).
 * Определяет, разрешено ли системе работать с инструментом.
 * Не отражает текущую активность — для этого существует {@link State}.
 */
public enum Status {

    /** Актив в архиве. Система игнорирует любые операции с ним. */
    DISABLED(0),
    /** Актив готов к работе. Система сама решает, что с ним делать (докачка/живой сбор). */
    ENABLED(1),
    /** Ручная блокировка для проведения технических работ или корректировки данных. */
    MAINTENANCE(2);

    private final int id;
    private static final Map<Integer, Status> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Status::getId, e -> e));}

    Status(int id) {this.id = id;}
    public int getId() {return id;}

    public static Status fromId(int id) {
        var status = BY_ID.get(id);
        if (status == null) throw new IllegalArgumentException("Unknown Status ID: " + id);
        return status;
    }
}