package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Order;
import java.util.List;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {

    public List<Order> findByBuyer(Long buyerId) {
        return list("buyer.id", buyerId);
    }

    public List<Order> findByStatus(Order.OrderStatus status) {
        return list("status", status);
    }
}
