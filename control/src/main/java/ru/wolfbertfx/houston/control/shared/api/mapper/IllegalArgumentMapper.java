package ru.wolfbertfx.houston.control.shared.api.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wolfbertfx.houston.control.shared.api.ErrorResponse;

/**
 * Некорректный вход клиента (неизвестный тикер/статус из enum-парсинга) → 400.
 * Сознательно глобальный: в этом сервисе IAE бросается только на границе разбора входа.
 */
@Provider
public class IllegalArgumentMapper implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger log = LoggerFactory.getLogger(IllegalArgumentMapper.class);

    @Override
    public Response toResponse(IllegalArgumentException e) {
        log.debug("Client input rejected: {}", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ErrorResponse.of(e.getMessage()))
                .build();
    }
}