package com.konstantion.controller;

import com.konstantion.bucket.Bucket;
import com.konstantion.order.OrderService;
import com.konstantion.order.dto.OrderDto;
import com.konstantion.user.User;
import org.springframework.stereotype.Controller;

@Controller
public record CliOrderController(OrderService orderService, User user, Bucket bucket) {
    public OrderDto createOrder() {
        return orderService.createOrder(user, bucket);
    }
}
