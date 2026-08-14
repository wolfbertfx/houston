package ru.radaps.houston.control;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ru.radaps.houston.common.asset.Ticker;

@Path("/hello")
public class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        //Ticker.CME_ES;
        return "Hello from Quarkus REST";
    }
}
