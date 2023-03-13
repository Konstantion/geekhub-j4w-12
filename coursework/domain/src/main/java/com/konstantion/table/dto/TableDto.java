package com.konstantion.table.dto;

import com.konstantion.order.Order;
import com.konstantion.table.TableType;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public record TableDto(UUID id, String name, Integer capacity, TableType tableType, Order order, List<User> waiters) {

}
