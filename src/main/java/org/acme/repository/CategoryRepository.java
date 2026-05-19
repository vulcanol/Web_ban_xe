package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Category;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {
}
