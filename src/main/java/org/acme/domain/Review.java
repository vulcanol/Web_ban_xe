package org.acme.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nguoi_viet_id", nullable = false)
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "nguoi_ban_id", nullable = false)
    private User seller;

    @ManyToOne
    @JoinColumn(name = "tin_dang_id")
    private Listing listing;

    @Column(name = "diem_danh_gia", nullable = false)
    private Integer rating;

    @Column(name = "noi_dung")
    private String content;

    @Column(name = "ngay_viet", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
