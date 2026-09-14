package ru.wolfbertfx.houston.control.venue.api.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.control.venue.api.dto.VenueSessionRequest;
import ru.wolfbertfx.houston.control.venue.api.dto.VenueSessionResponse;
import ru.wolfbertfx.houston.control.venue.api.mapper.VenueSessionApiMapper;
import ru.wolfbertfx.houston.control.venue.domain.VenueSession;
import ru.wolfbertfx.houston.control.venue.domain.VenueSessionRepository;
import ru.wolfbertfx.houston.control.shared.api.ErrorResponse;

import java.util.List;

@Path("/api/control/venue/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Venue Sessions", description = "Базовые торговые сессии площадок. Управление расписанием торговых сессий для каждой площадки.")
public class VenueSessionResource {

    @Inject
    VenueSessionRepository sessionRepo;

    @GET
    @Operation(
        operationId = "listVenueSessions",
        summary = "Получить все базовые сессии площадки",
        description = "Возвращает список всех базовых торговых сессий для указанной площадки. " +
                "Включает сессии для всех дней недели и фаз (PRE_MARKET, OPEN, POST_MARKET, CLOSED)."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Список сессий успешно получен",
            content = @Content(schema = @Schema(implementation = VenueSessionResponse.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "400", description = "Неверный venueId",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Площадка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<VenueSessionResponse> listByVenue(
            @Parameter(
                name = "venueId",
                description = "ID площадки (1=ICE, 2=MOEX, 3=NYMEX, 4=COMEX, 5=CME, 6=CBOE, 7=FOREX, 8=BINANCE)",
                required = true,
                example = "2",
                in = ParameterIn.QUERY,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1", maximum = "8")
            )
            @QueryParam("venueId") int venueId) {
        return VenueSessionApiMapper.toResponse(
                sessionRepo.findByVenue(Venue.fromId(venueId))
        );
    }

    @GET
    @Path("/{venueId}/day/{dayOfWeek}")
    @Operation(
        operationId = "listVenueSessionsByDay",
        summary = "Получить сессии площадки на конкретный день недели",
        description = "Возвращает торговые сессии для указанной площадки и дня недели. " +
                "День недели: 1=Понедельник, 2=Вторник, ..., 7=Воскресенье."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Список сессий для дня получен",
            content = @Content(schema = @Schema(implementation = VenueSessionResponse.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "400", description = "Неверные параметры (venueId или dayOfWeek вне диапазона)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Площадка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<VenueSessionResponse> listByVenueAndDay(
            @Parameter(
                name = "venueId",
                description = "ID площадки",
                required = true,
                example = "2",
                in = ParameterIn.PATH,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1", maximum = "8")
            )
            @PathParam("venueId") int venueId,
            @Parameter(
                name = "dayOfWeek",
                description = "День недели (1=Понедельник, ..., 7=Воскресенье)",
                required = true,
                example = "1",
                in = ParameterIn.PATH,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1", maximum = "7")
            )
            @PathParam("dayOfWeek") int dayOfWeek) {
        return VenueSessionApiMapper.toResponse(
                sessionRepo.findByVenueAndDay(ru.wolfbertfx.houston.common.venue.Venue.fromId(venueId), dayOfWeek)
        );
    }

    @POST
    @Transactional
    @Operation(
        operationId = "createVenueSession",
        summary = "Создать базовую сессию площадки",
        description = "Создаёт новую торговую сессию для площадки. " +
                "Если сессия на указанный день недели и фазу уже существует — вернёт ошибку 409. " +
                "Время указывается в часовом поясе площадки (venue timezone)."
    )
    @RequestBody(
        description = "Параметры создаваемой сессии",
        required = true,
        content = @Content(schema = @Schema(implementation = VenueSessionRequest.class))
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Сессия успешно создана",
            content = @Content(schema = @Schema(implementation = VenueSessionResponse.class))),
        @APIResponse(responseCode = "400", description = "Неверные параметры запроса (неверный venueId, phaseId, некорректное время)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Площадка или фаза не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "409", description = "Сессия на этот день недели и фазу уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response create(
            @RequestBody(
                description = "Данные для создания сессии",
                required = true,
                content = @Content(schema = @Schema(implementation = VenueSessionRequest.class))
            )
            @Valid VenueSessionRequest req) {
        var session = VenueSessionApiMapper.toDomain(req);
        var saved = sessionRepo.save(session);
        return Response.status(Response.Status.CREATED).entity(
                VenueSessionApiMapper.toResponse(saved)
        ).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(
        operationId = "updateVenueSession",
        summary = "Обновить базовую сессию",
        description = "Обновляет существующую торговую сессию. " +
                "Если сессия с указанным ID не найдена — вернёт 404."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Сессия успешно обновлена",
            content = @Content(schema = @Schema(implementation = VenueSessionResponse.class))),
        @APIResponse(responseCode = "400", description = "Неверные параметры",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Сессия не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "409", description = "Конфликт: сессия на этот день/фазу уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public VenueSessionResponse update(
            @Parameter(
                name = "id",
                description = "ID сессии",
                required = true,
                example = "1",
                in = ParameterIn.PATH,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1")
            )
            @PathParam("id") long id,
            @RequestBody(
                description = "Новые данные сессии",
                required = true,
                content = @Content(schema = @Schema(implementation = VenueSessionRequest.class))
            )
            @Valid VenueSessionRequest req) {
        var session = VenueSessionApiMapper.toDomain(req, id);
        var saved = sessionRepo.save(session);
        return VenueSessionApiMapper.toResponse(saved);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(
        operationId = "deleteVenueSession",
        summary = "Удалить базовую сессию",
        description = "Удаляет торговую сессию по ID. " +
                "Если сессия не найдена — вернёт 404."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Сессия успешно удалена"),
        @APIResponse(responseCode = "404", description = "Сессия не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response delete(
            @Parameter(
                name = "id",
                description = "ID сессии для удаления",
                required = true,
                example = "1",
                in = ParameterIn.PATH,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1")
            )
            @PathParam("id") long id) {
        sessionRepo.delete(new VenueSession(id, null, 0, null, null, null));
        return Response.noContent().build();
    }
}