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
import ru.wolfbertfx.houston.control.venue.api.dto.InstrumentSessionRequest;
import ru.wolfbertfx.houston.control.venue.api.dto.InstrumentSessionResponse;
import ru.wolfbertfx.houston.control.venue.api.mapper.InstrumentSessionApiMapper;
import ru.wolfbertfx.houston.control.venue.domain.InstrumentSession;
import ru.wolfbertfx.houston.control.venue.domain.InstrumentSessionRepository;
import ru.wolfbertfx.houston.control.shared.api.ErrorResponse;

import java.util.List;

@Path("/api/control/venue/instrument-sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Instrument Sessions", description = "Переопределения торговых сессий для конкретных инструментов или типов инструментов на площадках.")
public class InstrumentSessionResource {

    @Inject
    InstrumentSessionRepository repo;

    @GET
    @Operation(
        operationId = "listInstrumentSessions",
        summary = "Получить все переопределения сессий площадки",
        description = "Возвращает список всех переопределений торговых сессий для конкретных инструментов или типов инструментов на указанной площадке."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Список переопределений успешно получен",
            content = @Content(schema = @Schema(implementation = InstrumentSessionResponse.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "400", description = "Неверный venueId",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Площадка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<InstrumentSessionResponse> listByVenue(
            @Parameter(
                name = "venueId",
                description = "ID площадки (1=ICE, 2=MOEX, 3=NYMEX, 4=COMEX, 5=CME, 6=CBOE, 7=FOREX, 8=BINANCE)",
                required = true,
                example = "2",
                in = ParameterIn.QUERY,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1", maximum = "8")
            )
            @QueryParam("venueId") int venueId) {
        return InstrumentSessionApiMapper.toResponse(
                repo.findByVenue(ru.wolfbertfx.houston.common.venue.Venue.fromId(venueId))
        );
    }

    @GET
    @Path("/{venueId}/day/{dayOfWeek}")
    @Operation(
        operationId = "listInstrumentSessionsByDay",
        summary = "Получить переопределения сессий на конкретный день недели",
        description = "Возвращает переопределения торговых сессий для указанной площадки и дня недели. " +
                "День недели: 1=Понедельник, 2=Вторник, ..., 7=Воскресенье."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Список переопределений для дня получен",
            content = @Content(schema = @Schema(implementation = InstrumentSessionResponse.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "400", description = "Неверные параметры (venueId или dayOfWeek вне диапазона)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Площадка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<InstrumentSessionResponse> listByVenueAndDay(
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
        return InstrumentSessionApiMapper.toResponse(
                repo.findByVenueAndDay(ru.wolfbertfx.houston.common.venue.Venue.fromId(venueId), dayOfWeek)
        );
    }

    @POST
    @Transactional
    @Operation(
        operationId = "createInstrumentSession",
        summary = "Создать переопределение сессии",
        description = "Создаёт новое переопределение торговой сессии для конкретного инструмента или типа инструментов на площадке. " +
                "Если переопределение для этого инструмента/типа на указанный день/фазу уже существует — вернёт ошибку 409. " +
                "Время указывается в часовом поясе площадки (venue timezone)."
    )
    @RequestBody(
        description = "Параметры создаваемого переопределения сессии",
        required = true,
        content = @Content(schema = @Schema(implementation = InstrumentSessionRequest.class))
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Переопределение успешно создано",
            content = @Content(schema = @Schema(implementation = InstrumentSessionResponse.class))),
        @APIResponse(responseCode = "400", description = "Неверные параметры запроса (неверный venueId, instrumentId, typeId, phaseId, некорректное время)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Площадка, инструмент, тип или фаза не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "409", description = "Переопределение для этого инструмента/типа на этот день/фазу уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response create(
            @RequestBody(
                description = "Данные для создания переопределения сессии",
                required = true,
                content = @Content(schema = @Schema(implementation = InstrumentSessionRequest.class))
            )
            @Valid InstrumentSessionRequest req) {
        var session = ru.wolfbertfx.houston.control.venue.api.mapper.InstrumentSessionApiMapper.toDomain(req);
        var saved = repo.save(session);
        return Response.status(Response.Status.CREATED).entity(
                ru.wolfbertfx.houston.control.venue.api.mapper.InstrumentSessionApiMapper.toResponse(saved)
        ).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(
        operationId = "updateInstrumentSession",
        summary = "Обновить переопределение сессии",
        description = "Обновляет существующее переопределение торговой сессии. " +
                "Если переопределение с указанным ID не найдено — вернёт 404."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Переопределение успешно обновлено",
            content = @Content(schema = @Schema(implementation = InstrumentSessionResponse.class))),
        @APIResponse(responseCode = "400", description = "Неверные параметры",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Переопределение не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "409", description = "Конфликт: переопределение на этот день/фазу уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public InstrumentSessionResponse update(
            @Parameter(
                name = "id",
                description = "ID переопределения сессии",
                required = true,
                example = "1",
                in = ParameterIn.PATH,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1")
            )
            @PathParam("id") long id,
            @RequestBody(
                description = "Новые данные переопределения",
                required = true,
                content = @Content(schema = @Schema(implementation = InstrumentSessionRequest.class))
            )
            @Valid InstrumentSessionRequest req) {
        var session = ru.wolfbertfx.houston.control.venue.api.mapper.InstrumentSessionApiMapper.toDomain(req, id);
        var saved = repo.save(session);
        return ru.wolfbertfx.houston.control.venue.api.mapper.InstrumentSessionApiMapper.toResponse(saved);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(
        operationId = "deleteInstrumentSession",
        summary = "Удалить переопределение сессии",
        description = "Удаляет переопределение торговой сессии по ID. " +
                "Если переопределение не найдено — вернёт 404."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Переопределение успешно удалено"),
        @APIResponse(responseCode = "404", description = "Переопределение не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response delete(
            @Parameter(
                name = "id",
                description = "ID переопределения для удаления",
                required = true,
                example = "1",
                in = ParameterIn.PATH,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1")
            )
            @PathParam("id") long id) {
        repo.delete(new InstrumentSession(id, null, null, null, 0, null, null, null));
        return Response.noContent().build();
    }
}