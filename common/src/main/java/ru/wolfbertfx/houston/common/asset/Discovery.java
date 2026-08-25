package ru.wolfbertfx.houston.common.asset;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Где рождается справедливая цена инструмента относительно нашей площадки наблюдения.
 * Определяет эпистемологию гэпов на стыках сессий:
 * NATIVE — гэп есть само событие ценообразования, скрытых волн в нём нет;
 * ANCHORED — справедливая цена дрейфует вне нашей сессии, гэп может скрывать
 * структуру, восстанавливаемую по внешнему корреляту.
 */
public enum Discovery {

    /** Цена рождается только в сессии нашей площадки: гэп = событие (аукционное давление, переоценка). */
    NATIVE(1),
    /** Есть механическая ценовая связка с внешним рынком, торгующим без нашей сессии: гэп = экран над скрытым движением. */
    ANCHORED(2);

    private final int id;
    private static final Map<Integer, Discovery> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Discovery::getId, e -> e));}

    Discovery(int id) {this.id = id;}
    public int getId() {return id;}

    public static Discovery fromId(int id) {
        var discovery = BY_ID.get(id);
        if (discovery == null) throw new IllegalArgumentException("Unknown Discovery ID: " + id);
        return discovery;
    }
}