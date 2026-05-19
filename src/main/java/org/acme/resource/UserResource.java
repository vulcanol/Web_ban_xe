package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.User;
import org.acme.dto.UserDTO;
import org.acme.service.UserService;
import java.util.Optional;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @POST
    @Path("/register")
    public Response register(UserRegistrationRequest request) {
        try {
            User user = userService.registerUser(
                    request.fullName,
                    request.email,
                    request.password,
                    request.phoneNumber);
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        Optional<User> user = userService.authenticate(request.email, request.password);
        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse("Email hoặc mật khẩu không chính xác")).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("Người dùng không tồn tại")).build();
    }

    @GET
    public Response getAllUsers() {
        return Response.ok(userService.getAllUsers()).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return Response.ok(updatedUser).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    public static class UserRegistrationRequest {
        public String fullName;
        public String email;
        public String password;
        public String phoneNumber;
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
