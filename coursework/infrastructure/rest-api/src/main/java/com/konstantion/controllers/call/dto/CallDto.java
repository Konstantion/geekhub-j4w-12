package com.konstantion.controllers.call.dto;

import com.konstantion.call.Purpose;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CallDto(
        UUID id,
        UUID tableId,
        Purpose purpose,
        List<UUID> waitersId,
        LocalDateTime openedAt
) {
}
