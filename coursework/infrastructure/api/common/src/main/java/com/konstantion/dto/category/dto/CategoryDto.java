package com.konstantion.dto.category.dto;

import java.util.UUID;

public record CategoryDto(
        UUID id,
        String name,
        UUID creatorId
) {
}
