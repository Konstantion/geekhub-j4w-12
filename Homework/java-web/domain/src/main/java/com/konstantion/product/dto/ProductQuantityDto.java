package com.konstantion.product.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record ProductQuantityDto(UUID uuid, String name, Double price, String description, String category,
                                 String imagePath,
                                 LocalDateTime createdAt, UUID userUuid, Integer quantity) {
    public static ProductQuantityDto fromDto(ProductDto dto, Integer quantity) {
        return new ProductQuantityDto(dto.uuid(), dto.name(), dto.price(), dto.description(), dto.category(), dto.imagePath(), dto.createdAt(), dto.userUuid(), quantity);
    }

    public static ProductQuantityDto fromEntry(Map.Entry<ProductDto, Integer> entry) {
        return fromDto(entry.getKey(), entry.getValue());
    }
}
