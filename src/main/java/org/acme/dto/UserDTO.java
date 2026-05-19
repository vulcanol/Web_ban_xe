package org.acme.dto;

import java.time.Instant;

public class UserDTO {
    public Long id;
    public String fullName;
    public String email;
    public String phoneNumber;
    public String role;
    public String address;
    public String avatarUrl;
    public Boolean isActive;
    public Boolean emailVerified;
    public Instant createdAt;
    public Instant updatedAt;
}
