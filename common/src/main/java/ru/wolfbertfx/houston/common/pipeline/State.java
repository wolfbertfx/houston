package ru.wolfbertfx.houston.common.pipeline;

import ru.wolfbertfx.houston.common.asset.Status;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Состояние работы системы с активом (производное значение, вычисляется автоматически).
 * Машина состояний сбора данных: простой → докачка истории → живой режим.
 * Не является волей администратора — для этого существует {@link Status}.
 */
public enum State {

    /** Нет активной работы: актив отключен, не сконфигурирован или стоит на паузе. */
    IDLE(0),
    /** Идет докачка истории: гэп между watermark и текущим моментом велик, тянем чанками. */
    BACKFILL(1),
    /** Штатный режим: поллим текущие данные с заданным интервалом. */
    LIVE(2);

    private final int id;
    private static final Map<Integer, State> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(State::getId, e -> e));}

    State(int id) {this.id = id;}
    public int getId() {return id;}

    public static State fromId(int id) {
        var state = BY_ID.get(id);
        if (state == null) throw new IllegalArgumentException("Unknown State ID: " + id);
        return state;
    }
}