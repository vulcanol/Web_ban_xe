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
import org.acme.domain.Category;
import org.acme.domain.Listing;
import org.acme.service.CategoryService;
import org.acme.service.ListingService;

/**
 * Trang dành cho người dùng đã đăng nhập (KHACH_HANG, NGUOI_BAN).
 */
@Path("/user")
@Produces(MediaType.TEXT_HTML)
public class UserController {

    static final String SESSION_COOKIE = "SESSION_ID";

    @Inject SessionManager sessionManager;
    @Inject CategoryService categoryService;
    @Inject ListingService listingService;

    @CheckedTemplate(basePath = "user")
    public static class Templates {
        public static native TemplateInstance home(SessionData session,
                List<Category> categories, List<Listing> listings);

        public static native TemplateInstance profile(SessionData session);
    }

    private SessionData requireLogin(String sessionId) {
        return sessionManager.getSession(sessionId);
    }

    @GET
    @Path("/home")
    public Response home(@CookieParam(SESSION_COOKIE) String sessionId) {
        SessionData session = requireLogin(sessionId);
        if (session == null) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }

        List<Category> categories = categoryService.getAllCategoriesEntities();
        List<Listing> listings = listingService.getActiveListingEntities();
        return Response.ok(Templates.home(session, categories, listings)).build();
    }

    @GET
    @Path("/profile")
    public Response profile(@CookieParam(SESSION_COOKIE) String sessionId) {
        SessionData session = requireLogin(sessionId);
        if (session == null) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }
        return Response.ok(Templates.profile(session)).build();
    }
}
