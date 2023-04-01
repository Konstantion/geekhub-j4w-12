package com.konstantion.order;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID id);

    void delete(Order order);
}
