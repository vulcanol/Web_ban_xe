package org.acme.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.math.BigDecimal;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nguoi_mua_id", nullable = false)
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "tin_dang_id", nullable = false)
    private Listing listing;

    @Column(name = "gia_giao_dich", nullable = false)
    private BigDecimal transactionPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private OrderStatus status = OrderStatus.CHO_XAC_NHAN;

    @Column(name = "ghi_chu")
    private String notes;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
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

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public BigDecimal getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(BigDecimal transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public enum OrderStatus {
        CHO_XAC_NHAN, DANG_XU_LY, HOAN_THANH, HUY_BO
    }
}
