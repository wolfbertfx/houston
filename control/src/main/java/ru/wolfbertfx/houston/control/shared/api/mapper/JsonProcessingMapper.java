package ru.wolfbertfx.houston.control.shared.api.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wolfbertfx.houston.control.shared.api.ErrorResponse;

/**
 * Тело не десериализовалось (битый JSON, несовместимый тип поля) → 400.
 * Перехватывает базовый JsonProcessingException: покрывает весь иерархию ошибок Jackson
 * до входа в ресурсный метод.
 */
@Provider
public class JsonProcessingMapper implements ExceptionMapper<JsonProcessingException> {

    private static final Logger log = LoggerFactory.getLogger(JsonProcessingMapper.class);

    @Override
    public Response toResponse(JsonProcessingException e) {
        log.debug("Malformed request body rejected: {}", e.getOriginalMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ErrorResponse.of("Malformed request body: " + e.getOriginalMessage()))
                .build();
    }
}