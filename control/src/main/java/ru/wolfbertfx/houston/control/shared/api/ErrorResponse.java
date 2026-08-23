package ru.wolfbertfx.houston.control.shared.api;

/** Унифицированный контракт ошибки API. Общий для всех фич сервиса control. */
public record ErrorResponse(String error) {

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message);
    }
}