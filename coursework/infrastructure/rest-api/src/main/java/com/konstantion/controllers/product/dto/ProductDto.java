package com.konstantion.controllers.product.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDto(
        UUID id,
        String name,
        Double price,
        Double weight,
        UUID categoryId,
        String description,
        UUID creatorId,
        byte[] imageBytes,
        LocalDateTime createdAt,
        LocalDateTime deactivateAt,
        Boolean active
) {
}
