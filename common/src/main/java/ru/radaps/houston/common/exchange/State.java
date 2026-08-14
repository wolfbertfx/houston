/* Copyright (c) 2026 LLC "Radaps". All rights reserved. Internal Use Only. Confidential.*/
package com.radaps.ewaspace.houston.shared.exchange;

import com.radaps.ewaspace.houston.shared.Identifiable;
import com.radaps.ewaspace.houston.shared.Localizable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Состояние торговой сессии на биржевой площадке. */
public enum State implements Localizable, Identifiable {

    /** Рынок закрыт (вне торговой сессии). */
    CLOSED(0),
    /** Пре-маркет (подготовка к торгам, возможны лимитированные заявки). */
    PRE_MARKET(1),
    /** Рынок открыт (идет основная торговая сессия). */
    OPEN(2);

    private final int id;
    private static final Map<Integer, State> BY_ID;

    static {
        BY_ID = Arrays.stream(values()).collect(Collectors.toMap(State::getId, e -> e));
    }

    State(int id) {this.id = id;}

    public static State fromId(int id) {
        var status = BY_ID.get(id);
        if (status == null) throw new IllegalArgumentException("Unknown TradingStatus ID: " + id);
        return status;
    }

    @Override
    public int getId() {return id;}

    @Override
    public String getLocaleKey() {
        return "exchange.state." + this.name().toLowerCase();
    }
}
