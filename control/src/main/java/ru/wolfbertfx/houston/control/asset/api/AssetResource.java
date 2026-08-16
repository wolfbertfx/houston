package ru.wolfbertfx.houston.control.asset.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.wolfbertfx.houston.common.asset.Status;
import ru.wolfbertfx.houston.common.asset.Ticker;
import ru.wolfbertfx.houston.control.asset.AssetService;
import ru.wolfbertfx.houston.control.asset.domain.Asset;

import java.util.List;
import java.util.Map;

@Path("/api/control/assets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AssetResource {

    private final AssetService assetService;

    public AssetResource(AssetService assetService) {
        this.assetService = assetService;
    }

    @GET
    public List<Asset> listAssets() {
        return assetService.getAllAssets();
    }

    @PATCH
    @Path("/{ticker}/status")
    public Response updateStatus(@PathParam("ticker") String tickerStr, @QueryParam("value") String statusStr) {
        try {
            Ticker ticker = Ticker.valueOf(tickerStr.toUpperCase());
            Status status = Status.valueOf(statusStr.toUpperCase());
            Asset asset = assetService.updateStatus(ticker, status);
            return Response.ok(asset).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        }
    }
}
