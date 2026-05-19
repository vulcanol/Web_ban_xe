package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Brand;
import java.util.List;

@ApplicationScoped
public class BrandRepository implements PanacheRepository<Brand> {

    public List<Brand> findAllActive() {
        return list("isActive", true);
    }
}
