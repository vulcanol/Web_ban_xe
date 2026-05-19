package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Order;
import org.acme.service.OrderService;
import java.math.BigDecimal;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderService orderService;

    @GET
    @Path("/buyer/{buyerId}")
    public Response getOrdersByBuyer(@PathParam("buyerId") Long buyerId) {
        return Response.ok(orderService.getOrdersByBuyer(buyerId)).build();
    }

    @POST
    public Response createOrder(OrderRequest request) {
        try {
            Order order = orderService.createOrder(
                    request.buyerId, request.listingId, request.transactionPrice, request.notes);
            return Response.status(Response.Status.CREATED).entity(order).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}/status")
    public Response updateOrderStatus(@PathParam("id") Long id, StatusRequest request) {
        try {
            Order order = orderService.updateOrderStatus(id, request.status);
            return Response.ok(order).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    public static class OrderRequest {
        public Long buyerId;
        public Long listingId;
        public BigDecimal transactionPrice;
        public String notes;
    }

    public static class StatusRequest {
        public String status;
    }

    public static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
