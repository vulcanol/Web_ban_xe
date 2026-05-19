package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Order;
import org.acme.domain.User;
import org.acme.domain.Listing;
import org.acme.dto.OrderDTO;
import org.acme.repository.OrderRepository;
import org.acme.repository.UserRepository;
import org.acme.repository.ListingRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ListingRepository listingRepository;

    @Transactional
    public Order createOrder(Long buyerId, Long listingId, BigDecimal transactionPrice, String notes) {
        User buyer = userRepository.findById(buyerId);
        Listing listing = listingRepository.findById(listingId);

        if (buyer == null || listing == null) {
            throw new IllegalArgumentException("Người mua hoặc Tin đăng không tồn tại");
        }

        Order order = new Order();
        order.setBuyer(buyer);
        order.setListing(listing);
        order.setTransactionPrice(transactionPrice);
        order.setNotes(notes);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());

        orderRepository.persist(order);
        return order;
    }

    public List<OrderDTO> getOrdersByBuyer(Long buyerId) {
        return orderRepository.findByBuyer(buyerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Đơn hàng không tồn tại");
        }

        order.setStatus(Order.OrderStatus.valueOf(status));
        order.setUpdatedAt(Instant.now());
        orderRepository.persist(order);
        return order;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.id = order.getId();
        dto.buyerId = order.getBuyer().getId();
        dto.buyerName = order.getBuyer().getFullName();
        dto.listingId = order.getListing().getId();
        dto.listingTitle = order.getListing().getTitle();
        dto.transactionPrice = order.getTransactionPrice();
        dto.status = order.getStatus().name();
        dto.notes = order.getNotes();
        dto.createdAt = order.getCreatedAt();
        dto.updatedAt = order.getUpdatedAt();
        return dto;
    }
}
