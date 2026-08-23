package ru.wolfbertfx.houston.control.asset.domain;

import ru.wolfbertfx.houston.common.asset.Instrument;

/** Явный доменный контракт: актива нет в системе. Позволяет отличать 404 от 400 на границе API. */
public class AssetNotFoundException extends RuntimeException {

    public AssetNotFoundException(Instrument instrument) {
        super("Asset not found: " + instrument);
    }
}
