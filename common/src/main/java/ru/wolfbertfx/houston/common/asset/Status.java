package ru.wolfbertfx.houston.common.asset;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Административный статус актива.
 * Определяет, разрешено ли системе выполнять операции с инструментом (запись данных, анализ, стриминг).
 */
public enum Status {

    /** Активы в архиве. Система игнорирует любые операции с ним. */
    DISABLED(0),
    /** Идет процесс первичной настройки или импорта истории. */
    PREPARING(1),
    /** Актив готов к работе в штатном режиме. */
    ACTIVE(2),
    /** Ручная блокировка для проведения технических работ или корректировки данных. */
    MAINTENANCE(3);

    private final int id;
    private static final Map<Integer, Status> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Status::getId, e -> e));}

    Status(int id) {this.id = id;}
    public int getId() {return id;}

    public static Status fromId(int id) {
        var status = BY_ID.get(id);
        if (status == null) throw new IllegalArgumentException("Unknown AssetStatus ID: " + id);
        return status;
    }
}
