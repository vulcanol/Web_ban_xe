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
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Tự động tạo dữ liệu mẫu khi hệ thống khởi động lần đầu.
 *
 * Tài khoản Admin mặc định:
 *   Email   : admin
 *   Password: admin123
 */
@ApplicationScoped
public class DataInitializer {

    private static final Logger LOG = Logger.getLogger(DataInitializer.class);

    static final String DEFAULT_ADMIN_EMAIL    = "admin";
    static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    @Inject
    UserRepository userRepository;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        // ── 1. Tạo Admin mặc định ────────────────────────────────────────────
        if (userRepository.findByEmail(DEFAULT_ADMIN_EMAIL).isEmpty()) {
            User admin = new User();
            admin.setFullName("Quản Trị Viên");
            admin.setEmail(DEFAULT_ADMIN_EMAIL);
            admin.setPassword(hash(DEFAULT_ADMIN_PASSWORD));
            admin.setPhoneNumber("1900000000");
            admin.setRole(User.UserRole.ADMIN);
            admin.setIsActive(true);
            admin.setEmailVerified(true);
            admin.setCreatedAt(Instant.now());
            admin.setUpdatedAt(Instant.now());
            userRepository.persist(admin);
            LOG.info("🚀 Admin mặc định đã được tạo  →  email: admin  |  pass: admin123");
        } else {
            LOG.info("✅ Admin đã tồn tại, bỏ qua khởi tạo admin.");
        }

        // ── 2. Tạo users mẫu (chỉ khi DB chưa có người dùng nào ngoài admin) ──
        if (userRepository.count() <= 1) {
            createSampleUsers();
            LOG.info("🌱 Dữ liệu mẫu người dùng đã được khởi tạo.");
        }
    }

    private void createSampleUsers() {
        // Người bán 1
        createUser("Nguyễn Văn An",    "an.nguyen@email.com",   "password123",
                   "0901234567", "12 Lê Lợi, Quận 1, TP. Hồ Chí Minh",
                   User.UserRole.NGUOI_BAN, true,  true,  -30);

        // Người bán 2
        createUser("Lê Văn Cường",     "cuong.le@email.com",    "password123",
                   "0923456789", "45 Trần Phú, Quận Hải Châu, Đà Nẵng",
                   User.UserRole.NGUOI_BAN, true,  true,  -20);

        // Người bán 3
        createUser("Phạm Minh Tuấn",   "tuan.pham@email.com",   "password123",
                   "0934567890", "78 Nguyễn Huệ, Quận Hoàn Kiếm, Hà Nội",
                   User.UserRole.NGUOI_BAN, true,  false, -15);

        // Khách hàng 1
        createUser("Trần Thị Bình",    "binh.tran@email.com",   "password123",
                   "0912345678", "34 Lý Thường Kiệt, Quận Thanh Xuân, Hà Nội",
                   User.UserRole.KHACH_HANG, true, true,  -25);

        // Khách hàng 2
        createUser("Hoàng Thị Mai",    "mai.hoang@email.com",   "password123",
                   "0945678901", "56 Cách Mạng Tháng 8, Quận 3, TP. Hồ Chí Minh",
                   User.UserRole.KHACH_HANG, true, true,  -18);

        // Khách hàng 3
        createUser("Vũ Đức Thắng",     "thang.vu@email.com",    "password123",
                   "0956789012", "23 Bạch Đằng, Quận Bình Thạnh, TP. Hồ Chí Minh",
                   User.UserRole.KHACH_HANG, true, false, -10);

        // Khách hàng 4 (bị khóa - để test)
        createUser("Đỗ Quang Huy",     "huy.do@email.com",      "password123",
                   "0967890123", null,
                   User.UserRole.KHACH_HANG, false, false, -5);

        // Người bán mới (chưa xác minh)
        createUser("Bùi Thị Lan",      "lan.bui@email.com",     "password123",
                   "0978901234", "90 Điện Biên Phủ, Quận Đống Đa, Hà Nội",
                   User.UserRole.NGUOI_BAN, true,  false, -2);
    }

    private void createUser(String fullName, String email, String password,
                            String phone, String address,
                            User.UserRole role, boolean isActive, boolean emailVerified,
                            long daysOffset) {
        if (userRepository.findByEmail(email).isPresent()) return;

        User u = new User();
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPassword(hash(password));
        u.setPhoneNumber(phone);
        u.setAddress(address);
        u.setRole(role);
        u.setIsActive(isActive);
        u.setEmailVerified(emailVerified);
        u.setCreatedAt(Instant.now().plus(daysOffset, ChronoUnit.DAYS));
        u.setUpdatedAt(Instant.now().plus(daysOffset, ChronoUnit.DAYS));
        userRepository.persist(u);
    }

    private String hash(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
