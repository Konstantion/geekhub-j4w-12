package com.konstantion.dto.product;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDto(UUID uuid, String name, Double price,
                         String description,
                         LocalDateTime createdAt, UUID userUuid,
                         UUID categoryUuid, byte[] imageBytes) {
}
