package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Listing;
import org.acme.service.ListingService;
import java.math.BigDecimal;

@Path("/api/listings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ListingResource {

    @Inject
    ListingService listingService;

    @GET
    public Response getActiveListings() {
        return Response.ok(listingService.getActiveListings()).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getListingsByUser(@PathParam("userId") Long userId) {
        return Response.ok(listingService.getListingsByUser(userId)).build();
    }

    @GET
    @Path("/province/{province}")
    public Response getListingsByProvince(@PathParam("province") String province) {
        return Response.ok(listingService.getListingsByProvince(province)).build();
    }

    @GET
    @Path("/price")
    public Response getListingsByPriceRange(
            @QueryParam("minPrice") long minPrice,
            @QueryParam("maxPrice") long maxPrice) {
        return Response.ok(listingService.getListingsByPriceRange(minPrice, maxPrice)).build();
    }

    @POST
    public Response createListing(ListingRequest request) {
        try {
            Listing listing = listingService.createListing(
                    request.userId, request.carId, request.title, request.price,
                    request.mileage, request.condition, request.color, request.licensePlate,
                    request.description, request.province, request.district);
            return Response.status(Response.Status.CREATED).entity(listing).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @POST
    @Path("/{id}/view")
    public Response incrementViewCount(@PathParam("id") Long id) {
        listingService.incrementViewCount(id);
        return Response.ok().build();
    }

    public static class ListingRequest {
        public Long userId;
        public Long carId;
        public String title;
        public BigDecimal price;
        public Integer mileage;
        public String condition;
        public String color;
        public String licensePlate;
        public String description;
        public String province;
        public String district;
    }

    public static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
