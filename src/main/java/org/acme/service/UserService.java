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
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        userRepository.persist(user);
        return user;
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
