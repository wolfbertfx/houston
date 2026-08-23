package ru.wolfbertfx.houston.control.shared.api.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wolfbertfx.houston.control.asset.domain.AssetNotFoundException;
import ru.wolfbertfx.houston.control.shared.api.ErrorResponse;

/**
 * Транслирует доменное исключение в HTTP-контракт: актив не найден → 404.
 * WARN сознательно: в норме каталог синхронизируется с БД на старте,
 * поэтому эта ошибка — сигнал дрейфа данных, а не ожидаемый ответ.
 */
@Provider
public class AssetNotFoundMapper implements ExceptionMapper<AssetNotFoundException> {

    private static final Logger log = LoggerFactory.getLogger(AssetNotFoundMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(AssetNotFoundException e) {
        log.warn("Asset not found [path={}]: {}", uriInfo != null ? uriInfo.getPath() : "?", e.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ErrorResponse.of(e.getMessage()))
                .build();
    }
}