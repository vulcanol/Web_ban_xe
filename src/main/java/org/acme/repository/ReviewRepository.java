package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Review;
import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheRepository<Review> {

    public List<Review> findBySeller(Long sellerId) {
        return list("seller.id", sellerId);
    }

    public List<Review> findByListing(Integer listingId) {
        return list("listing.id", listingId);
    }

    public double getSellerAverageRating(Long sellerId) {
        // Calculate average rating using stream
        return list("seller.id", sellerId).stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
