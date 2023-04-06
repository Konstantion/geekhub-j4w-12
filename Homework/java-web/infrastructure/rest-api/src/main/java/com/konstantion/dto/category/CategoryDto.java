package com.konstantion.dto.category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryDto(
        UUID uuid,
        String name,
        LocalDateTime createdAt,
        UUID userUuid
) {
}
