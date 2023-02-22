package com.konstantion.order.dto;

import com.konstantion.product.Product;

import java.util.Map;

public record OrderDto(Long id, Long customerId, Map<Product, Integer> products, Integer totalPrice) {
}
