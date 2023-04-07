package com.konstantion.order;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    void delete(Order order);

    List<Order> findAll();

    List<Order> findByUserId(UUID uuid);

    Optional<Order> findById(UUID uuid);

    Order save(Order object);

    void updateOrderStatusById(UUID uuid, OrderStatus status);

    OrderStatus findStatusById(UUID uuid);

    default Order saveAndFlush(Order order) {
        return save(order);
    }
}
