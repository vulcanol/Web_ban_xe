package org.acme.resource;

import java.net.URI;
import java.util.List;

import org.acme.domain.Category;
import org.acme.service.CategoryService;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin")
@Produces(MediaType.TEXT_HTML)
public class WebUIController {

    @Inject
    CategoryService categoryService;

    @CheckedTemplate(basePath = "")
    public static class Templates {
        public static native TemplateInstance index();

        public static native TemplateInstance categories(List<Category> categories);

        public static native TemplateInstance categoryForm(boolean isEdit, Category category);
    }

    @GET
    public TemplateInstance getIndex() {
        return Templates.index();
    }

    // Route /admin/index rõ ràng
    @GET
    @Path("/index")
    public TemplateInstance getIndexPage() {
        return Templates.index();
    }

    @GET
    @Path("/categories")
    public TemplateInstance listCategories() {
        List<Category> categories = categoryService.getAllCategoriesEntities();
        return Templates.categories(categories);
    }

    @GET
    @Path("/categories/new")
    public TemplateInstance newCategoryForm() {
        return Templates.categoryForm(false, new Category());
    }

    @GET
    @Path("/categories/{id}/edit")
    public TemplateInstance editCategoryForm(@PathParam("id") Long id) {
        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        return Templates.categoryForm(true, category);
    }

    @POST
    @Path("/categories/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createCategory(
            @FormParam("name") String name,
            @FormParam("description") String description,
            @FormParam("iconUrl") String iconUrl) {

        categoryService.createCategory(name, description, iconUrl);

        return Response.seeOther(URI.create("/admin/categories"))
                .build();
    }

    @POST
    @Path("/categories/{id}/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateCategory(
            @PathParam("id") Long id,
            @FormParam("name") String name,
            @FormParam("description") String description,
            @FormParam("iconUrl") String iconUrl) {

        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            throw new NotFoundException("Category not found");
        }

        category.setName(name);
        category.setDescription(description);
        category.setIconUrl(iconUrl);

        categoryService.updateCategory(category);

        return Response.seeOther(URI.create("/admin/categories"))
                .build();
    }

    @GET
    @Path("/categories/{id}/delete")
    public Response deleteCategory(@PathParam("id") Long id) {
        categoryService.deleteCategory(id);

        return Response.seeOther(URI.create("/admin/categories"))
                .build();
    }
}