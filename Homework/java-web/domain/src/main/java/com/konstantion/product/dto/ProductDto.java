package com.konstantion.product.dto;

import java.time.LocalDateTime;

public record ProductDto(Long id, String name, Integer price, LocalDateTime createdAt) {
}
