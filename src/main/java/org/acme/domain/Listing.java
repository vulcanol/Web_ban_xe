package org.acme.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.math.BigDecimal;

@Entity
@Table(name = "listings")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "xe_id", nullable = false)
    private Car car;

    @Column(name = "tieu_de", length = 255, nullable = false)
    private String title;

    @Column(name = "gia_ban", nullable = false)
    private BigDecimal price;

    @Column(name = "so_km")
    private Integer mileage = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "tinh_trang", nullable = false)
    private Condition condition = Condition.CU;

    @Column(name = "mau_sac", length = 50)
    private String color;

    @Column(name = "bien_so", length = 20)
    private String licensePlate;

    @Column(name = "mo_ta")
    private String description;

    @Column(name = "tinh_thanh", length = 100)
    private String province;

    @Column(name = "quan_huyen", length = 100)
    private String district;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private ListingStatus status = ListingStatus.CHO_DUYET;

    @Column(name = "luot_xem")
    private Integer viewCount = 0;

    @Column(name = "gio_het_han")
    private Instant expiryTime;

    @Column(name = "ngay_dang", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "ngay_cap_nhat", nullable = false)
    private Instant updatedAt = Instant.now();

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public ListingStatus getStatus() {
        return status;
    }

    public void setStatus(ListingStatus status) {
        this.status = status;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Instant getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Instant expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public enum Condition {
        MOI, CU
    }

    public enum ListingStatus {
        CHO_DUYET, DANG_DANG, DA_BAN, HET_HAN, BI_AN
    }
}
