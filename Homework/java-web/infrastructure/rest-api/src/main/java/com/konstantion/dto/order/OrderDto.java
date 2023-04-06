package com.konstantion.dto.order;

import com.konstantion.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record OrderDto(UUID uuid, Map<UUID, Integer> products, LocalDateTime placedAt, Double totalPrice, UUID userUuid, OrderStatus status) {
}
