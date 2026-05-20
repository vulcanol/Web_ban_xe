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
import org.acme.dto.UserDTO;
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

    @CheckedTemplate(basePath = "admin", requireTypeSafeExpressions = false)
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
                List<UserDTO> users);

        public static native TemplateInstance userForm(SessionData session,
                boolean isEdit, UserDTO user, String error);
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
    @Path("/users/new")
    public Response newUserForm(@CookieParam(SESSION_COOKIE) String sessionId) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);
        return Response.ok(Templates.userForm(session, false, new UserDTO())).build();
    }

    @POST
    @Path("/users/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createUser(@CookieParam(SESSION_COOKIE) String sessionId,
            @FormParam("fullName") String fullName,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("phoneNumber") String phoneNumber,
            @FormParam("address") String address,
            @FormParam("avatarUrl") String avatarUrl,
            @FormParam("role") String role) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        try {
            User.UserRole userRole = parseRole(role);
            userService.createAdminUser(fullName, email, password, phoneNumber,
                    address, avatarUrl, userRole);
            return Response.seeOther(URI.create("/admin/users")).build();
        } catch (IllegalArgumentException e) {
            // Return back to form with error
            UserDTO dto = buildDtoFromParams(null, fullName, email, phoneNumber, address, avatarUrl, role, true, false);
            return Response.ok(Templates.userForm(session, false, dto, e.getMessage())).build();
        }
    }

    @GET
    @Path("/users/{id}/edit")
    public Response editUserForm(@CookieParam(SESSION_COOKIE) String sessionId,
            @PathParam("id") Long id) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        return userService.getUserById(id)
                .map(user -> {
                    String noError = null;
                    UserDTO dto = new UserDTO();
                    dto.id = user.getId();
                    dto.fullName = user.getFullName();
                    dto.email = user.getEmail();
                    dto.phoneNumber = user.getPhoneNumber();
                    dto.role = user.getRole().name();
                    dto.address = user.getAddress();
                    dto.avatarUrl = user.getAvatarUrl();
                    dto.isActive = user.getIsActive();
                    dto.emailVerified = user.getEmailVerified();
                    dto.createdAt = user.getCreatedAt();
                    dto.updatedAt = user.getUpdatedAt();
                    return Response.ok(Templates.userForm(session, true, dto, noError)).build();
                })
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @POST
    @Path("/users/{id}/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateUser(@CookieParam(SESSION_COOKIE) String sessionId,
            @PathParam("id") Long id,
            @FormParam("fullName") String fullName,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("phoneNumber") String phoneNumber,
            @FormParam("address") String address,
            @FormParam("avatarUrl") String avatarUrl,
            @FormParam("role") String role,
            @FormParam("isActive") String isActiveStr,
            @FormParam("emailVerified") String emailVerifiedStr) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        try {
            User.UserRole userRole = parseRole(role);
            Boolean isActive = "true".equals(isActiveStr) || "on".equals(isActiveStr);
            Boolean emailVerified = "true".equals(emailVerifiedStr) || "on".equals(emailVerifiedStr);

            userService.updateUserAdmin(id, fullName, email,
                    (password != null && !password.isBlank()) ? password : null,
                    phoneNumber, address, avatarUrl, userRole, isActive, emailVerified);
            return Response.seeOther(URI.create("/admin/users")).build();
        } catch (IllegalArgumentException e) {
            UserDTO dto = buildDtoFromParams(id, fullName, email, phoneNumber, address, avatarUrl, role,
                    "on".equals(isActiveStr) || "true".equals(isActiveStr),
                    "on".equals(emailVerifiedStr) || "true".equals(emailVerifiedStr));
            return Response.ok(Templates.userForm(session, true, dto, e.getMessage())).build();
        }
    }

    @GET
    @Path("/users/{id}/delete")
    public Response deleteUser(@CookieParam(SESSION_COOKIE) String sessionId,
            @PathParam("id") Long id) {
        SessionData session = requireAdmin(sessionId);
        if (session == null) return unauthorizedRedirect(sessionId);

        try {
            userService.deleteUser(id);
        } catch (IllegalArgumentException e) {
            // Silently ignore if not found or is admin; could add flash message
        }
        return Response.seeOther(URI.create("/admin/users")).build();
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

    // ── HELPERS ───────────────────────────────────────────────────────────────

    private User.UserRole parseRole(String role) {
        if (role == null || role.isBlank()) return User.UserRole.KHACH_HANG;
        try {
            return User.UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return User.UserRole.KHACH_HANG;
        }
    }

    private UserDTO buildDtoFromParams(Long id, String fullName, String email,
            String phoneNumber, String address, String avatarUrl,
            String role, Boolean isActive, Boolean emailVerified) {
        UserDTO dto = new UserDTO();
        dto.id = id;
        dto.fullName = fullName;
        dto.email = email;
        dto.phoneNumber = phoneNumber;
        dto.address = address;
        dto.avatarUrl = avatarUrl;
        dto.role = role;
        dto.isActive = isActive;
        dto.emailVerified = emailVerified;
        return dto;
    }
}
