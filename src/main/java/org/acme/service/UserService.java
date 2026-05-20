package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.User;
import org.acme.dto.UserDTO;
import org.acme.repository.UserRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public User registerUser(String fullName, String email, String password, String phoneNumber) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(hashPassword(password));
        user.setPhoneNumber(phoneNumber);
        user.setRole(User.UserRole.KHACH_HANG);
        user.setIsActive(true);
        user.setEmailVerified(false);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        userRepository.persist(user);
        return user;
    }

    /**
     * Admin tạo người dùng mới (có thể chọn vai trò)
     */
    @Transactional
    public User createAdminUser(String fullName, String email, String password,
                                String phoneNumber, String address, String avatarUrl,
                                User.UserRole role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(hashPassword(password));
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setAvatarUrl(avatarUrl);
        user.setRole(role != null ? role : User.UserRole.KHACH_HANG);
        user.setIsActive(true);
        user.setEmailVerified(false);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        userRepository.persist(user);
        return user;
    }

    /**
     * Admin cập nhật thông tin người dùng (có thể đổi vai trò, trạng thái, mật khẩu)
     */
    @Transactional
    public User updateUserAdmin(Long id, String fullName, String email,
                                String newPassword, String phoneNumber,
                                String address, String avatarUrl,
                                User.UserRole role, Boolean isActive, Boolean emailVerified) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Người dùng không tồn tại");
        }

        // Check email uniqueness if changed
        if (!user.getEmail().equals(email)) {
            Optional<User> existing = userRepository.findByEmail(email);
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new IllegalArgumentException("Email đã được sử dụng bởi tài khoản khác");
            }
        }

        if (fullName != null && !fullName.isBlank()) user.setFullName(fullName);
        if (email != null && !email.isBlank()) user.setEmail(email);
        if (newPassword != null && !newPassword.isBlank()) user.setPassword(hashPassword(newPassword));
        if (phoneNumber != null) user.setPhoneNumber(phoneNumber.isBlank() ? null : phoneNumber);
        if (address != null) user.setAddress(address.isBlank() ? null : address);
        if (avatarUrl != null) user.setAvatarUrl(avatarUrl.isBlank() ? null : avatarUrl);
        if (role != null) user.setRole(role);
        if (isActive != null) user.setIsActive(isActive);
        if (emailVerified != null) user.setEmailVerified(emailVerified);

        user.setUpdatedAt(Instant.now());
        userRepository.persist(user);
        return user;
    }

    /**
     * Xóa người dùng (không cho phép xóa admin)
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Người dùng không tồn tại");
        }
        if (User.UserRole.ADMIN.equals(user.getRole())) {
            throw new IllegalArgumentException("Không thể xóa tài khoản Admin");
        }
        userRepository.delete(user);
    }

    public Optional<User> authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && verifyPassword(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Người dùng không tồn tại");
        }

        if (updatedUser.getFullName() != null) {
            user.setFullName(updatedUser.getFullName());
        }
        if (updatedUser.getPhoneNumber() != null) {
            user.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        if (updatedUser.getAddress() != null) {
            user.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getAvatarUrl() != null) {
            user.setAvatarUrl(updatedUser.getAvatarUrl());
        }

        user.setUpdatedAt(Instant.now());
        userRepository.persist(user);
        return user;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findByIdOptional(id);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long countUsers() {
        return userRepository.count();
    }

    @Transactional
    public void toggleUserActive(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            user.setIsActive(!Boolean.TRUE.equals(user.getIsActive()));
            userRepository.persist(user);
        }
    }

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    private boolean verifyPassword(String password, String hash) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
    }

    private UserDTO convertToDTO(User user) {
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
        return dto;
    }
}
