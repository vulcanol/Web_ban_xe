package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Category;
import org.acme.service.CategoryService;

@Path("/api/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    CategoryService categoryService;

    @GET
    public Response getAllCategories() {
        return Response.ok(categoryService.getAllCategories()).build();
    }

    @POST
    public Response createCategory(CategoryRequest request) {
        Category category = categoryService.createCategory(request.name, request.description, request.iconUrl);
        return Response.status(Response.Status.CREATED).entity(category).build();
    }

    public static class CategoryRequest {
        public String name;
        public String description;
        public String iconUrl;
    }
}
