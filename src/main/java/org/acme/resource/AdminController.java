package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.acme.auth.SessionManager;
import org.acme.auth.SessionManager.SessionData;
import org.acme.domain.Brand;
import org.acme.domain.Category;
import org.acme.domain.User;
import org.acme.service.BrandService;
import org.acme.service.CategoryService;
import org.acme.service.ListingService;
import org.acme.service.UserService;

/**
 * Trang quản trị — chỉ dành cho ADMIN.
 * Tất cả routes đều kiểm tra session + role.
 */
@Path("/admin")
@Produces(MediaType.TEXT_HTML)
public class AdminController {

    static final String SESSION_COOKIE = "SESSION_ID";

    @Inject SessionManager sessionManager;
    @Inject CategoryService categoryService;
    @Inject BrandService brandService;
    @Inject UserService userService;
    @Inject ListingService listingService;

    @CheckedTemplate(basePath = "admin")
    public static class Templates {
        public static native TemplateInstance dashboard(SessionData session,
                long totalUsers, long totalListings, long totalCategories, long totalBrands);

        public static native TemplateInstance categories(SessionData session,
                List<Category> categories);

        public static native TemplateInstance categoryForm(SessionData session,
                boolean isEdit, Category category);

        public static native TemplateInstance brands(SessionData session,
                List<Brand> brands);

        public static native TemplateInstance users(SessionData session,
                List<org.acme.dto.UserDTO> users);
    }

    // ── GUARD: kiểm tra quyền admin ─────────────────────────────────────────

    private SessionData requireAdmin(String sessionId) {
        SessionData session = sessionManager.getSession(sessionId);
        if (session == null) return null;
        if (!"ADMIN".equals(session.role())) return null;
        return session;
    }

    private Response unauthorizedRedirect(String sessionId) {
        if (!sessionManager.isLoggedIn(sessionId)) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }
        // User đã đăng nhập nhưng không phải admin
        return Response.seeOther(URI.create("/user/home")).build();
    }

    // ── DASHBOARD ────────────────────────────────────────────────────────────

    @GET
    public Response dashboard(@CookieParam(SESSION_COOKIE) String sessionId) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        long totalUsers = userService.countUsers();
        long totalListings = listingService.countActiveListings();
        long totalCategories = categoryService.countCategories();
        long totalBrands = brandService.countBrands();

        return Response.ok(Templates.dashboard(session,
                totalUsers, totalListings, totalCategories, totalBrands)).build();
    }

    // ── CATEGORIES ────────────────────────────────────────────────────────────

    @GET
    @Path("/categories")
    public Response listCategories(@CookieParam(SESSION_COOKIE) String sessionId) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        List<Category> categories = categoryService.getAllCategoriesEntities();
        return Response.ok(Templates.categories(session, categories)).build();
    }

    @GET
    @Path("/categories/new")
    public Response newCategoryForm(@CookieParam(SESSION_COOKIE) String sessionId) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);
        return Response.ok(Templates.categoryForm(session, false, new Category())).build();
    }

    @GET
    @Path("/categories/{id}/edit")
    public Response editCategoryForm(@CookieParam(SESSION_COOKIE) String sessionId,
            @PathParam("id") Long id) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        Category category = categoryService.getCategoryById(id);
        if (category == null) throw new NotFoundException("Category not found");
        return Response.ok(Templates.categoryForm(session, true, category)).build();
    }

    @POST
    @Path("/categories/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createCategory(@CookieParam(SESSION_COOKIE) String sessionId,
            @FormParam("name") String name,
            @FormParam("description") String description,
            @FormParam("iconUrl") String iconUrl) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        categoryService.createCategory(name, description, iconUrl);
        return Response.seeOther(URI.create("/admin/categories")).build();
    }

    @POST
    @Path("/categories/{id}/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateCategory(@CookieParam(SESSION_COOKIE) String sessionId,
            @PathParam("id") Long id,
            @FormParam("name") String name,
            @FormParam("description") String description,
            @FormParam("iconUrl") String iconUrl) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        Category category = categoryService.getCategoryById(id);
        if (category == null) throw new NotFoundException("Category not found");
        category.setName(name);
        category.setDescription(description);
        category.setIconUrl(iconUrl);
        categoryService.updateCategory(category);
        return Response.seeOther(URI.create("/admin/categories")).build();
    }

    @GET
    @Path("/categories/{id}/delete")
    public Response deleteCategory(@CookieParam(SESSION_COOKIE) String sessionId,
            @PathParam("id") Long id) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        categoryService.deleteCategory(id);
        return Response.seeOther(URI.create("/admin/categories")).build();
    }

    // ── BRANDS ───────────────────────────────────────────────────────────────

    @GET
    @Path("/brands")
    public Response listBrands(@CookieParam(SESSION_COOKIE) String sessionId) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        List<Brand> brands = brandService.getAllBrandEntities();
        return Response.ok(Templates.brands(session, brands)).build();
    }

    // ── USERS ─────────────────────────────────────────────────────────────────

    @GET
    @Path("/users")
    public Response listUsers(@CookieParam(SESSION_COOKIE) String sessionId) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        return Response.ok(Templates.users(session, userService.getAllUsers())).build();
    }

    @GET
    @Path("/users/{id}/toggle")
    public Response toggleUserStatus(@CookieParam(SESSION_COOKIE) String sessionId,
            @PathParam("id") Long id) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        userService.toggleUserActive(id);
        return Response.seeOther(URI.create("/admin/users")).build();
    }
}
