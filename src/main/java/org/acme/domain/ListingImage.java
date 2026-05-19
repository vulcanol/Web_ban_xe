package org.acme.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "listing_images")
public class ListingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tin_dang_id", nullable = false)
    private Listing listing;

    @Column(name = "url_anh", length = 500, nullable = false)
    private String imageUrl;

    @Column(name = "la_anh_chinh")
    private Boolean isMainImage = false;

    @Column(name = "thu_tu")
    private Integer orderIndex = 0;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsMainImage() {
        return isMainImage;
    }

    public void setIsMainImage(Boolean isMainImage) {
        this.isMainImage = isMainImage;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
}
