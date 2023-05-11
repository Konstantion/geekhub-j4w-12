package com.konstantion.dto.hall.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record HallDto(
        UUID id,
        String name,
        Boolean active,
        LocalDateTime createdAt
) {
}
