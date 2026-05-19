package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.ListingImage;
import java.util.List;

@ApplicationScoped
public class ListingImageRepository implements PanacheRepository<ListingImage> {

    public List<ListingImage> findByListing(Integer listingId) {
        return list("listing.id", listingId);
    }

    public ListingImage findMainImageByListing(Integer listingId) {
        return find("listing.id = ?1 and isMainImage = true", listingId).firstResult();
    }
}
