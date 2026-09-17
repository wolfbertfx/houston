package ru.wolfbertfx.houston.control.asset.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ru.wolfbertfx.houston.common.asset.Ticker;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.common.asset.Segment;
import ru.wolfbertfx.houston.common.venue.Venue;
import ru.wolfbertfx.houston.control.asset.AssetService;
import ru.wolfbertfx.houston.control.asset.api.mapper.AssetApiMapper;
import ru.wolfbertfx.houston.control.asset.api.dto.AssetResponse;
import ru.wolfbertfx.houston.control.asset.api.dto.UpdateAssetStatusRequest;
import ru.wolfbertfx.houston.control.shared.api.ApiParams;
import ru.wolfbertfx.houston.control.shared.api.ErrorResponse;

import java.util.List;

@Path("/api/control/assets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Assets", description = "Жизненный цикл активов в системе")

public class AssetResource {

    private final AssetService assetService;

    public AssetResource(AssetService assetService) {
        this.assetService = assetService;
    }

    @GET
    @Path("/")
    @Operation(operationId = "listAssets", summary = "Список активов с фильтрами",
    description = "Возвращает активы. Все параметры опциональны — не передан = все значения.")
    @APIResponse(responseCode = "200", description = "Список активов", content = @Content(
    mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = AssetResponse.class)))
    @APIResponse(responseCode = "400", description = "Неверный ID enum'а",
    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)))

    public List<AssetResponse> listAssets(
        @QueryParam("status") @Parameter(description = "ID статуса: 0=DISABLED, 1=PREPARING, 2=ENABLED") Integer statusId,
        @QueryParam("venue") @Parameter(description = "ID биржи: 0=MOEX, 1=ICE, 2=NYMEX, 3=COMEX, 4=CBOE, 5=CME") Integer venueId,
        @QueryParam("segment") @Parameter(description = "ID сегмента: 0=EQUITY, 1=BOND, 2=OIL_GAS, 3=MINING, 4=TECH, 5=FINANCE, 6=RETAIL, 7=UTILITIES, 8=METALS, 9=ENERGY, 10=MAJORS, 11=REGIONAL") Integer segmentId) {
        
        var status = ApiParams.parseEnumId(Status.class, statusId, "status");
        var venue = ApiParams.parseEnumId(Venue.class, venueId, "venue");
        var segment = ApiParams.parseEnumId(Segment.class, segmentId, "segment");
        return AssetApiMapper.toResponses(assetService.getAssets(status, venue, segment));
    }

    @PATCH
    @Path("/{tickerId}/status")
    @Operation(operationId = "updateAssetStatus", summary = "Смена статуса актива",
    description = "Управляет жизненным циклом актива. Режим работы системы " +
    "(докачка/живой сбор) система определяет сама и возвращает в поле State.")
    @Parameter(name = "tickerId", description = "Числовой ID инструмента",
    required = true, example = "10020")
    @APIResponse(responseCode = "200", description = "Статус обновлён",
    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AssetResponse.class)))
    @APIResponse(responseCode = "400", description = "Неизвестный tickerId или statusId",
    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Актив отсутствует в системе",
    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)))

    public AssetResponse updateStatus(@PathParam("tickerId") int tickerId, UpdateAssetStatusRequest request) {
        var asset = assetService.updateStatus(Ticker.fromId(tickerId), request.toStatus());
        return AssetApiMapper.toResponse(asset);
    }
}