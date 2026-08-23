package ru.wolfbertfx.houston.control.asset.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.wolfbertfx.houston.common.asset.Catalog;
import ru.wolfbertfx.houston.control.asset.AssetService;
import ru.wolfbertfx.houston.control.asset.api.dto.AssetApiMapper;
import ru.wolfbertfx.houston.control.asset.api.dto.AssetResponse;
import ru.wolfbertfx.houston.control.asset.api.dto.ErrorResponse;
import ru.wolfbertfx.houston.control.asset.api.dto.UpdateAssetStatusRequest;
import ru.wolfbertfx.houston.control.asset.domain.AssetNotFoundException;

import java.util.List;

@Path("/api/control/assets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AssetResource {

    private final AssetService assetService;

    public AssetResource(AssetService assetService) {
        this.assetService = assetService;
    }

    @GET
    public List<AssetResponse> listAssets() {
        return AssetApiMapper.toResponses(assetService.getAllAssets());
    }

    @PATCH
    @Path("/{ticker}/status")
    public Response updateStatus(@PathParam("ticker") String tickerStr, UpdateAssetStatusRequest request) {
        var ticker = parseTicker(tickerStr);
        if (ticker == null) {
            return badRequest("Unknown ticker: " + tickerStr);
        }
        try {
            var asset = assetService.updateStatus(ticker, request.toStatus());
            return Response.ok(AssetApiMapper.toResponse(asset)).build();
        } catch (AssetNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(ErrorResponse.of(e.getMessage())).build();
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage());
        }
    }

    private Catalog parseTicker(String value) {
        try {
            return Catalog.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Response badRequest(String message) {
        return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse.of(message)).build();
    }
}
