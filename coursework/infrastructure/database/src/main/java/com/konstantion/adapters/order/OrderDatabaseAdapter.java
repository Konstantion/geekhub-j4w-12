package com.konstantion.adapters.order;

import com.konstantion.order.Order;
import com.konstantion.order.OrderPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public record OrderDatabaseAdapter() implements OrderPort {
    @Override
    public Order save(Order order) {
        return null;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public void delete(Order order) {

    }
}
