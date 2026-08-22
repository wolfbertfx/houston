package ru.wolfbertfx.houston.common.asset;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Текущий статус работы системы с активом (производное значение, вычисляется автоматически).
 * Определяет, чем система сейчас занимается с инструментом. Не является волей администратора —
 * для этого существует {@link Mode}.
 */
public enum Status {

    /** Нет активной работы: актив отключен, не сконфигурирован или стоит на паузе. */
    IDLE(0),
    /** Идет докачка истории: гэп между watermark и текущим моментом велик, тянем чанками. */
    BACKFILL(1),
    /** Штатный режим: поллим текущие данные с заданным интервалом. */
    LIVE(2);

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