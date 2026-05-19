package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Listing;
import java.util.List;

@ApplicationScoped
public class ListingRepository implements PanacheRepository<Listing> {

    public List<Listing> findActiveListings() {
        return list("status", Listing.ListingStatus.DANG_DANG);
    }

    public List<Listing> findByUser(Long userId) {
        return list("user.id", userId);
    }

    public List<Listing> findByProvince(String province) {
        return list("province", province);
    }

    public List<Listing> findByPriceRange(long minPrice, long maxPrice) {
        return list("price >= ?1 and price <= ?2", minPrice, maxPrice);
    }

    public List<Listing> findByStatus(Listing.ListingStatus status) {
        return list("status", status);
    }
}
