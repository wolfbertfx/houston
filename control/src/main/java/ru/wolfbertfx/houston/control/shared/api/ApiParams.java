package ru.wolfbertfx.houston.control.shared.api;

import java.lang.reflect.Array;

/**
 * Утилиты для разбора и валидации входных параметров REST API.
 * Бросает {@link IllegalArgumentException} — маппится в 400 через {@link IllegalArgumentMapper}.
 */
public final class ApiParams {

    private ApiParams() {}

    /**
     * Парсит числовой ID в enum-константу.
     *
     * @param enumClass класс enum'а
     * @param id        числовой ID (может быть null)
     * @param paramName имя параметра для сообщения об ошибке
     * @param <E>       тип enum'а
     * @return enum-константа или null, если id == null
     * @throws IllegalArgumentException если id вне допустимого диапазона [0, values.length-1]
     */
    public static <E extends Enum<E>> E parseEnumId(Class<E> enumClass, Integer id, String paramName) {
        if (id == null) return null;
        E[] values = enumClass.getEnumConstants();
        if (id < 0 || id >= values.length) {
            throw new IllegalArgumentException("Invalid " + paramName + " ID: " + id +
                ". Valid range: 0-" + (values.length - 1));
        }
        return values[id];
    }
}