package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Car;
import java.util.List;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car> {

    public List<Car> findByBrand(Long brandId) {
        return list("brand.id", brandId);
    }

    public List<Car> findByCategory(Long categoryId) {
        return list("category.id", categoryId);
    }

    public List<Car> findByBrandAndCategory(Long brandId, Long categoryId) {
        return list("brand.id = ?1 and category.id = ?2", brandId, categoryId);
    }
}
