package com.konstantion.order;

import com.konstantion.order.model.OrderProductsRequest;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getAll(boolean onlyActive);

    default List<Order> getAll() {
        return getAll(true);
    }

    Order getById(UUID id);

    Order transferToAnotherTable(UUID orderId, UUID tableId, User user);

    Order open(UUID tableId, User user);

    Order close(UUID orderId, User user);

    Order delete(UUID orderId, User user);

    int addProduct(UUID orderId, OrderProductsRequest request, User user);

    int removeProduct(UUID orderId, OrderProductsRequest request, User user);
}
