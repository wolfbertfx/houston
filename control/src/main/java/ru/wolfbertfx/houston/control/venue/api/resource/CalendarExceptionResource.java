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
import ru.wolfbertfx.houston.control.venue.api.dto.CalendarExceptionRequest;
import ru.wolfbertfx.houston.control.venue.api.dto.CalendarExceptionResponse;
import ru.wolfbertfx.houston.control.venue.api.mapper.CalendarExceptionApiMapper;
import ru.wolfbertfx.houston.control.venue.domain.CalendarException;
import ru.wolfbertfx.houston.control.venue.domain.CalendarExceptionRepository;
import ru.wolfbertfx.houston.control.shared.api.ErrorResponse;

import java.time.LocalDate;
import java.util.List;

@Path("/api/control/venue/exceptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Calendar Exceptions", description = "Исключения календаря (праздники, закрытия, сокращённые дни, кастомные расписания)")
public class CalendarExceptionResource {

    @Inject
    CalendarExceptionRepository repo;

    @GET
    @Operation(
        operationId = "listCalendarExceptions",
        summary = "Получить все исключения календаря площадки",
        description = "Возвращает список всех исключений календаря (праздники, закрытия, сокращённые дни) для указанной площадки."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Список исключений успешно получен",
            content = @Content(schema = @Schema(implementation = CalendarExceptionResponse.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "400", description = "Неверный venueId",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Площадка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<CalendarExceptionResponse> listByVenue(
            @Parameter(
                name = "venueId",
                description = "ID площадки (1=ICE, 2=MOEX, 3=NYMEX, 4=COMEX, 5=CME, 6=CBOE, 7=FOREX, 8=BINANCE)",
                required = true,
                example = "2",
                in = ParameterIn.QUERY,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1", maximum = "8")
            )
            @QueryParam("venueId") int venueId) {
        return CalendarExceptionApiMapper.toResponse(
                repo.findByVenue(ru.wolfbertfx.houston.common.venue.Venue.fromId(venueId))
        );
    }

    @GET
    @Path("/{venueId}/range")
    @Operation(
        operationId = "listCalendarExceptionsByRange",
        summary = "Получить исключения календаря в диапазоне дат",
        description = "Возвращает исключения календаря для указанной площадки в заданном диапазоне дат (включительно). " +
                "Даты указываются в формате ISO-8601 (YYYY-MM-DD)."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Список исключений в диапазоне получен",
            content = @Content(schema = @Schema(implementation = CalendarExceptionResponse.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "400", description = "Неверные параметры (venueId, from, to)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Площадка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<CalendarExceptionResponse> listByVenueAndDateRange(
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
                name = "from",
                description = "Начальная дата диапазона (включительно), формат YYYY-MM-DD",
                required = true,
                example = "2024-01-01",
                in = ParameterIn.QUERY,
                schema = @Schema(type = SchemaType.STRING, format = "date")
            )
            @QueryParam("from") LocalDate from,
            @Parameter(
                name = "to",
                description = "Конечная дата диапазона (включительно), формат YYYY-MM-DD",
                required = true,
                example = "2024-12-31",
                in = ParameterIn.QUERY,
                schema = @Schema(type = SchemaType.STRING, format = "date")
            )
            @QueryParam("to") LocalDate to) {
        return CalendarExceptionApiMapper.toResponse(
                repo.findByVenueAndDateRange(
                        ru.wolfbertfx.houston.common.venue.Venue.fromId(venueId),
                        from, to
                )
        );
    }

    @POST
    @Transactional
    @Operation(
        operationId = "createCalendarException",
        summary = "Создать исключение календаря",
        description = "Создаёт новое исключение календаря (праздник, закрытие, сокращённый день, кастомное расписание) для площадки. " +
                "Если исключение на эти даты для этого инструмента/типа уже существует — вернёт ошибку 409. " +
                "Даты указываются в календаре площадки (venue timezone)."
    )
    @RequestBody(
        description = "Параметры создаваемого исключения календаря",
        required = true,
        content = @Content(schema = @Schema(implementation = CalendarExceptionRequest.class))
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Исключение успешно создано",
            content = @Content(schema = @Schema(implementation = CalendarExceptionResponse.class))),
        @APIResponse(responseCode = "400", description = "Неверные параметры запроса (неверный venueId, instrumentId, typeId, даты, тип исключения)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Площадка, инструмент, тип или тип исключения не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "409", description = "Исключение на эти даты для этого инструмента/типа уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response create(
            @RequestBody(
                description = "Параметры создаваемого исключения календаря",
                required = true,
                content = @Content(schema = @Schema(implementation = CalendarExceptionRequest.class))
            )
            @Valid CalendarExceptionRequest req) {
        var exc = ru.wolfbertfx.houston.control.venue.api.mapper.CalendarExceptionApiMapper.toDomain(req);
        var saved = repo.save(exc);
        return Response.status(Response.Status.CREATED).entity(
                ru.wolfbertfx.houston.control.venue.api.mapper.CalendarExceptionApiMapper.toResponse(saved)
        ).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(
        operationId = "updateCalendarException",
        summary = "Обновить исключение календаря",
        description = "Обновляет существующее исключение календаря. " +
                "Если исключение с указанным ID не найдено — вернёт 404."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Исключение успешно обновлено",
            content = @Content(schema = @Schema(implementation = CalendarExceptionResponse.class))),
        @APIResponse(responseCode = "400", description = "Неверные параметры",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "404", description = "Исключение не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @APIResponse(responseCode = "409", description = "Конфликт: исключение на эти даты для этого инструмента/типа уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public CalendarExceptionResponse update(
            @Parameter(
                name = "id",
                description = "ID исключения календаря",
                required = true,
                example = "1",
                in = ParameterIn.PATH,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1")
            )
            @PathParam("id") long id,
            @RequestBody(
                description = "Новые данные исключения",
                required = true,
                content = @Content(schema = @Schema(implementation = CalendarExceptionRequest.class))
            )
            @Valid CalendarExceptionRequest req) {
        var exc = ru.wolfbertfx.houston.control.venue.api.mapper.CalendarExceptionApiMapper.toDomain(req, id);
        var saved = repo.save(exc);
        return ru.wolfbertfx.houston.control.venue.api.mapper.CalendarExceptionApiMapper.toResponse(saved);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(
        operationId = "deleteCalendarException",
        summary = "Удалить исключение календаря",
        description = "Удаляет исключение календаря по ID. " +
                "Если исключение не найдено — вернёт 404."
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Исключение успешно удалено"),
        @APIResponse(responseCode = "404", description = "Исключение не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response delete(
            @Parameter(
                name = "id",
                description = "ID исключения для удаления",
                required = true,
                example = "1",
                in = ParameterIn.PATH,
                schema = @Schema(type = SchemaType.INTEGER, minimum = "1")
            )
            @PathParam("id") long id) {
        repo.delete(new CalendarException(id, null, null, null, null, null, null, null));
        return Response.noContent().build();
    }
}