package com.konstantion.order;

import com.konstantion.bucket.Bucket;
import com.konstantion.order.dto.OrderDto;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> findAll();
    OrderDto createOrder(User user, Bucket bucket);

    OrderDto findOrderById(UUID uuid);

    OrderDto deleteOrderById(UUID uuid);

    List<OrderDto> findOrdersByUserId(UUID uuid);

    void completeOrder(UUID uuid);
}
