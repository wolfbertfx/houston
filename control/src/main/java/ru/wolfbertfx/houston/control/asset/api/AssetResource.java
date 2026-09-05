package ru.wolfbertfx.houston.control.asset.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ru.wolfbertfx.houston.common.asset.Instrument;
import ru.wolfbertfx.houston.control.asset.AssetService;
import ru.wolfbertfx.houston.control.asset.api.mapper.AssetApiMapper;
import ru.wolfbertfx.houston.control.asset.api.dto.AssetResponse;
import ru.wolfbertfx.houston.control.asset.api.dto.UpdateAssetStatusRequest;
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
    @Operation(operationId = "listAssets", summary = "Список всех активов",
    description = "Возвращает полный каталог активов с их статусами и статическими метаданными.")
    @APIResponse(responseCode = "200", description = "Каталог активов", content = @Content(
    mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = AssetResponse.class)))

    public List<AssetResponse> listAssets() {
        return AssetApiMapper.toResponses(assetService.getAllAssets());
    }

    @PATCH
    @Path("/{instrumentId}/status")
    @Operation(operationId = "updateAssetStatus", summary = "Смена статуса актива",
    description = "Управляет жизненным циклом актива. Режим работы системы " +
    "(докачка/живой сбор) система определяет сама и возвращает в поле State.")
    @Parameter(name = "instrumentId", description = "Числовой ID инструмента",
    required = true, example = "10020")
    @APIResponse(responseCode = "200", description = "Статус обновлён",
    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AssetResponse.class)))
    @APIResponse(responseCode = "400", description = "Неизвестный instrumentId или statusId",
    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Актив отсутствует в системе",
    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)))

    public AssetResponse updateStatus(@PathParam("instrumentId") int instrumentId, UpdateAssetStatusRequest request) {
        var asset = assetService.updateStatus(Instrument.fromId(instrumentId), request.toStatus());
        return AssetApiMapper.toResponse(asset);
    }
}