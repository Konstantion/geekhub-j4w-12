package com.konstantion.order;

import com.konstantion.bucket.Bucket;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> findAll();

    Order createOrder(User user, Bucket bucket);

    Order findOrderById(UUID uuid);

    Order deleteOrderById(UUID uuid);

    List<Order> findOrdersByUserId(UUID uuid);

    void completeOrder(UUID uuid);
}
