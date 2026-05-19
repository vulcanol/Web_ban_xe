package org.acme.dto;

import java.time.Instant;
import java.math.BigDecimal;

public class ListingDTO {
    public Integer id;
    public Long userId;
    public String sellerName;
    public Integer carId;
    public String carName;
    public String title;
    public BigDecimal price;
    public Integer mileage;
    public String condition;
    public String color;
    public String licensePlate;
    public String description;
    public String province;
    public String district;
    public String status;
    public Integer viewCount;
    public Instant expiryTime;
    public Instant createdAt;
    public Instant updatedAt;
}
