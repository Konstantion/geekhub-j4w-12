package com.konstantion.order.dto;

import com.konstantion.order.OrderStatus;

import java.util.Map;
import java.util.UUID;

public record OrderDto(UUID uuid, Map<UUID, Integer> products, Double totalPrice, UUID userUuid, OrderStatus status) {
}
