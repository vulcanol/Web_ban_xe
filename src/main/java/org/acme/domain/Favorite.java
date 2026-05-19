package org.acme.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tin_dang_id", nullable = false)
    private Listing listing;

    @Column(name = "ngay_luu", nullable = false, updatable = false)
    private Instant savedAt = Instant.now();

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

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Instant getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Instant savedAt) {
        this.savedAt = savedAt;
    }
}
