package ru.wolfbertfx.houston.control.venue.domain;

/**
 * Тип исключения календаря.
 */
public enum ExceptionType {
    /** Полное закрытие площадки/инструмента */
    FULL_CLOSE,
    /** Сокращённые часы — используется customSchedule */
    REDUCED_HOURS,
    /** Полностью кастомное расписание на день */
    CUSTOM_SCHEDULE
}