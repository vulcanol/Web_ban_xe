package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Car;
import org.acme.service.CarService;

@Path("/api/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarResource {

    @Inject
    CarService carService;

    @GET
    public Response getAllCars() {
        return Response.ok(carService.getAllCars()).build();
    }

    @GET
    @Path("/brand/{brandId}")
    public Response getCarsByBrand(@PathParam("brandId") Long brandId) {
        return Response.ok(carService.getCarsByBrand(brandId)).build();
    }

    @GET
    @Path("/category/{categoryId}")
    public Response getCarsByCategory(@PathParam("categoryId") Long categoryId) {
        return Response.ok(carService.getCarsByCategory(categoryId)).build();
    }

    @POST
    public Response createCar(CarRequest request) {
        try {
            Car car = carService.createCar(
                    request.brandId, request.categoryId, request.name, request.yearManufactured,
                    request.fuelType, request.transmission, request.seats, request.engine,
                    request.horsePower, request.displacement, request.design);
            return Response.status(Response.Status.CREATED).entity(car).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    public static class CarRequest {
        public Long brandId;
        public Long categoryId;
        public String name;
        public Integer yearManufactured;
        public String fuelType;
        public String transmission;
        public Integer seats;
        public String engine;
        public Float horsePower;
        public Float displacement;
        public String design;
    }

    public static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
