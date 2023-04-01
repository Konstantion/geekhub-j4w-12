package com.konstantion.order;

import com.konstantion.order.dto.OrderDto;
import com.konstantion.user.User;

import java.util.UUID;

public interface OrderService {
    OrderDto getById(UUID id);
    OrderDto getTableOrder(UUID tableId, User user);
    OrderDto transferToAnotherTable(UUID orderId, UUID tableId, User user);
    OrderDto open(UUID tableId, User user);
    OrderDto close(UUID orderId, User user);
    OrderDto addProduct(UUID orderId, UUID productId, Integer quantity, User user);
    OrderDto removeProduct(UUID orderId, UUID productId, Integer quantity, User user);
}
