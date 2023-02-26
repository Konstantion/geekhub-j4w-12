package com.konstantion.product.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDto(UUID uuid, String name, Double price, LocalDateTime createdAt, UUID userUuid) {
}
