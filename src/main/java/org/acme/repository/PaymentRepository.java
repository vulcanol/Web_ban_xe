package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Payment;
import java.util.Optional;

@ApplicationScoped
public class PaymentRepository implements PanacheRepository<Payment> {

    public Optional<Payment> findByTransactionCode(String transactionCode) {
        return find("transactionCode", transactionCode).firstResultOptional();
    }

    public Payment findByOrder(Integer orderId) {
        return find("order.id", orderId).firstResult();
    }
}
