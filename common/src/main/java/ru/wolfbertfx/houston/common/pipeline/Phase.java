package ru.wolfbertfx.houston.common.pipeline;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Фаза торговой сессии на биржевой площадке. */
public enum Phase {

    /** Рынок закрыт (вне торговой сессии). */
    CLOSED(0),
    /** Пре-маркет (подготовка к торгам, возможны лимитированные заявки). */
    PRE_MARKET(1),
    /** Рынок открыт (идет основная торговая сессия). */
    OPEN(2);

    private final int id;
    private static final Map<Integer, Phase> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Phase::getId, e -> e));}

    Phase(int id) {this.id = id;}
    public int getId() {return id;}

    public static Phase fromId(int id) {
        var phase = BY_ID.get(id);
        if (phase == null) throw new IllegalArgumentException("Unknown SessionPhase ID: " + id);
        return phase;
    }
}