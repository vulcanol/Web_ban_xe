package org.acme.auth;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.Optional;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.acme.domain.User;
import org.acme.service.UserService;

/**
 * Xử lý đăng nhập, đăng ký và đăng xuất.
 * Cookie "SESSION_ID" được dùng để theo dõi phiên đăng nhập.
 */
@Path("/auth")
@Produces(MediaType.TEXT_HTML)
public class AuthController {

    static final String SESSION_COOKIE = "SESSION_ID";

    @Inject
    UserService userService;

    @Inject
    SessionManager sessionManager;

    @CheckedTemplate(basePath = "auth")
    public static class Templates {
        public static native TemplateInstance login(String error);
        public static native TemplateInstance register(String error);
    }

    // ── ĐĂNG NHẬP ───────────────────────────────────────────────────────────

    @GET
    @Path("/login")
    public Response loginPage(@CookieParam(SESSION_COOKIE) String sessionId) {
        // Nếu đã đăng nhập thì redirect về trang phù hợp
        if (sessionManager.isLoggedIn(sessionId)) {
            return redirectByRole(sessionId);
        }
        return Response.ok(Templates.login(null)).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response doLogin(
            @FormParam("email") String email,
            @FormParam("password") String password) {

        Optional<User> userOpt = userService.authenticate(email, password);

        if (userOpt.isEmpty()) {
            return Response.ok(Templates.login("Email hoặc mật khẩu không chính xác")).build();
        }

        User user = userOpt.get();
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            return Response.ok(Templates.login("Tài khoản của bạn đã bị khóa")).build();
        }

        String sessionId = sessionManager.createSession(
                user.getId(), user.getEmail(), user.getFullName(), user.getRole().name());

        NewCookie cookie = new NewCookie.Builder(SESSION_COOKIE)
                .value(sessionId)
                .path("/")
                .maxAge(3600 * 8)
                .httpOnly(true)
                .build();

        return redirectByRole(sessionId, cookie);
    }

    // ── ĐĂNG KÝ ─────────────────────────────────────────────────────────────

    @GET
    @Path("/register")
    public Response registerPage(@CookieParam(SESSION_COOKIE) String sessionId) {
        if (sessionManager.isLoggedIn(sessionId)) {
            return redirectByRole(sessionId);
        }
        return Response.ok(Templates.register(null)).build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response doRegister(
            @FormParam("fullName") String fullName,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("phoneNumber") String phoneNumber) {
        try {
            userService.registerUser(fullName, email, password, phoneNumber);
            return Response.seeOther(URI.create("/auth/login?registered=true")).build();
        } catch (IllegalArgumentException e) {
            return Response.ok(Templates.register(e.getMessage())).build();
        }
    }

    // ── ĐĂNG XUẤT ───────────────────────────────────────────────────────────

    @GET
    @Path("/logout")
    public Response logout(@CookieParam(SESSION_COOKIE) String sessionId) {
        sessionManager.removeSession(sessionId);
        NewCookie expiredCookie = new NewCookie.Builder(SESSION_COOKIE)
                .value("")
                .path("/")
                .maxAge(0)
                .build();
        return Response.seeOther(URI.create("/auth/login"))
                .cookie(expiredCookie)
                .build();
    }

    // ── HELPER ───────────────────────────────────────────────────────────────

    private Response redirectByRole(String sessionId) {
        if (sessionManager.isAdmin(sessionId)) {
            return Response.seeOther(URI.create("/admin")).build();
        }
        return Response.seeOther(URI.create("/user/home")).build();
    }

    private Response redirectByRole(String sessionId, NewCookie cookie) {
        if (sessionManager.isAdmin(sessionId)) {
            return Response.seeOther(URI.create("/admin")).cookie(cookie).build();
        }
        return Response.seeOther(URI.create("/user/home")).cookie(cookie).build();
    }
}
