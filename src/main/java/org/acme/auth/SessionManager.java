package org.acme.auth;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quản lý session người dùng trong bộ nhớ.
 * Session được xác định bằng sessionId lưu trong Cookie.
 */
@ApplicationScoped
public class SessionManager {

    private static final long SESSION_TIMEOUT_SECONDS = 3600 * 8; // 8 giờ

    private final Map<String, SessionData> sessions = new ConcurrentHashMap<>();

    public String createSession(Long userId, String email, String fullName, String role) {
        String sessionId = UUID.randomUUID().toString();
        SessionData data = new SessionData(userId, email, fullName, role,
                Instant.now().plusSeconds(SESSION_TIMEOUT_SECONDS));
        sessions.put(sessionId, data);
        return sessionId;
    }

    public SessionData getSession(String sessionId) {
        if (sessionId == null) return null;
        SessionData data = sessions.get(sessionId);
        if (data == null) return null;
        // Kiểm tra hết hạn
        if (Instant.now().isAfter(data.expiresAt())) {
            sessions.remove(sessionId);
            return null;
        }
        return data;
    }

    public void removeSession(String sessionId) {
        if (sessionId != null) {
            sessions.remove(sessionId);
        }
    }

    public boolean isAdmin(String sessionId) {
        SessionData data = getSession(sessionId);
        return data != null && "ADMIN".equals(data.role());
    }

    public boolean isLoggedIn(String sessionId) {
        return getSession(sessionId) != null;
    }

    /** Dữ liệu session bất biến */
    public record SessionData(
            Long userId,
            String email,
            String fullName,
            String role,
            Instant expiresAt
    ) {}
}
