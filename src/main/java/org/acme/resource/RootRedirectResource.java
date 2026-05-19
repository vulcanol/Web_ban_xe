package org.acme.resource;

import java.net.URI;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * Redirect từ "/" sang "/admin" để truy cập giao diện web
 */
@Path("/")
public class RootRedirectResource {

    @GET
    public Response redirectToAdmin() {
        return Response.seeOther(URI.create("/admin")).build();
    }
}
