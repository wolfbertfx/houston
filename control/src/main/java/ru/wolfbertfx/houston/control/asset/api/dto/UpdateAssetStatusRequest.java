package ru.wolfbertfx.houston.control.asset.api.dto;

import ru.wolfbertfx.houston.common.asset.Status;

/**
 * Команда смены статуса актива. Несёт числовой ID статуса — тот же контракт, что и весь rest API.
 * Обязательна как POJO: JSON-тело в JAX-RS связывается только через объект.
 */
public record UpdateAssetStatusRequest(int statusId) {

    public Status toStatus() {
        return Status.fromId(statusId);
    }
}
