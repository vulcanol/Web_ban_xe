package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Favorite;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FavoriteRepository implements PanacheRepository<Favorite> {

    public List<Favorite> findByUser(Long userId) {
        return list("user.id", userId);
    }

    public Optional<Favorite> findByUserAndListing(Long userId, Integer listingId) {
        return find("user.id = ?1 and listing.id = ?2", userId, listingId).firstResultOptional();
    }

    public Boolean isFavorited(Long userId, Integer listingId) {
        return findByUserAndListing(userId, listingId).isPresent();
    }
}
