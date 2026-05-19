package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Brand;
import org.acme.service.BrandService;

@Path("/api/brands")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BrandResource {

    @Inject
    BrandService brandService;

    @GET
    public Response getAllBrands() {
        return Response.ok(brandService.getAllBrands()).build();
    }

    @GET
    @Path("/active")
    public Response getActiveBrands() {
        return Response.ok(brandService.getActiveBrands()).build();
    }

    @POST
    public Response createBrand(BrandRequest request) {
        Brand brand = brandService.createBrand(request.name, request.country, request.logoUrl);
        return Response.status(Response.Status.CREATED).entity(brand).build();
    }

    public static class BrandRequest {
        public String name;
        public String country;
        public String logoUrl;
    }
}
