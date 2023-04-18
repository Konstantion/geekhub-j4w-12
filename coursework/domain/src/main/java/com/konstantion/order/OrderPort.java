package com.konstantion.order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderPort {
    Order save(Order order);

    Optional<Order> findById(UUID id);

    void delete(Order order);

    List<Order> findAll();
}
