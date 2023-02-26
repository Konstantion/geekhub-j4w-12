package com.konstantion.order.dto;

import com.konstantion.product.Product;

import java.util.Map;
import java.util.UUID;

public record OrderDto(UUID uuid, Map<Product, Integer> products, Double totalPrice, UUID userUuid) {
}
