package com.konstantion.order;

import com.konstantion.bucket.Bucket;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> findAll();

    Order createOrder(User user, Bucket bucket);

    Order findOrderById(UUID uuid, User user);

    Order deleteOrder(UUID uuid);

    List<Order> findOrdersByUserId(UUID uuid, User user);

    UUID completeOrder(UUID uuid, User user);
    UUID cancelOrder(UUID uuid, User user);
}
