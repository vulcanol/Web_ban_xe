package org.acme.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.User;
import org.acme.repository.UserRepository;
import org.jboss.logging.Logger;

/**
 * Tự động tạo tài khoản admin mặc định khi hệ thống khởi động
 * nếu chưa có tài khoản admin nào.
 *
 * Thông tin đăng nhập mặc định:
 *   Email   : admin@banxe.vn
 *   Password: Admin@123
 */
@ApplicationScoped
public class DataInitializer {

    private static final Logger LOG = Logger.getLogger(DataInitializer.class);

    static final String DEFAULT_ADMIN_EMAIL = "admin";
    static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    @Inject
    UserRepository userRepository;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        if (userRepository.findByEmail(DEFAULT_ADMIN_EMAIL).isPresent()) {
            LOG.info("✅ Admin account already exists: " + DEFAULT_ADMIN_EMAIL);
            return;
        }

        User admin = new User();
        admin.setFullName("Quản Trị Viên");
        admin.setEmail(DEFAULT_ADMIN_EMAIL);
        admin.setPassword(BCrypt.withDefaults().hashToString(12, DEFAULT_ADMIN_PASSWORD.toCharArray()));
        admin.setRole(User.UserRole.ADMIN);
        admin.setIsActive(true);
        admin.setEmailVerified(true);

        userRepository.persist(admin);

        LOG.info("🚀 Default admin account created!");
        LOG.info("   Email   : " + DEFAULT_ADMIN_EMAIL);
        LOG.info("   Password: " + DEFAULT_ADMIN_PASSWORD);
    }
}
