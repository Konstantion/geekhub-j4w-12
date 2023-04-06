package com.konstantion.order;

import com.konstantion.user.User;

import java.util.UUID;

public interface OrderService {
    Order getById(UUID id);
    Order getTableOrder(UUID tableId, User user);
    Order transferToAnotherTable(UUID orderId, UUID tableId, User user);
    Order open(UUID tableId, User user);
    Order close(UUID orderId, User user);
    Order addProduct(UUID orderId, UUID productId, Integer quantity, User user);
    Order removeProduct(UUID orderId, UUID productId, Integer quantity, User user);
}
