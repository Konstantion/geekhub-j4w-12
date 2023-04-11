package com.konstantion.controllers.category.dto;

import java.util.UUID;

public record CategoryDto(
        UUID id,
        String name,
        UUID creatorId
) {
}
