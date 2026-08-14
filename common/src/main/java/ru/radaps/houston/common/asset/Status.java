/* Copyright (c) 2026 LLC "Radaps". All rights reserved. Internal Use Only. Confidential.*/
package com.radaps.ewaspace.houston.shared.asset;

import com.radaps.ewaspace.houston.shared.Identifiable;
import com.radaps.ewaspace.houston.shared.Localizable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Административный статус актива.
 * Определяет, разрешено ли системе выполнять операции с инструментом (запись данных, анализ, стриминг).
 */
public enum Status implements Localizable, Identifiable {

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
    private static final Map<String, Status> BY_NAME;

    static {
        BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Status::getId, e -> e));
        BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(e -> e.name().toUpperCase(), e -> e));
    }

    Status(int id) {this.id = id;}

    public static Status fromId(int id) {
        var status = BY_ID.get(id);
        if (status == null) throw new IllegalArgumentException("Unknown AssetStatus ID: " + id);
        return status;
    }

    public static Status fromString(String name) {
        var status = BY_NAME.get(name.toUpperCase());
        if (status == null) throw new IllegalArgumentException("Unknown AssetStatus name: " + name);
        return status;
    }

    @Override
    public int getId() {return id;}

    @Override
    public String getLocaleKey() {
        return "asset.status." + this.name().toLowerCase();
    }
}
