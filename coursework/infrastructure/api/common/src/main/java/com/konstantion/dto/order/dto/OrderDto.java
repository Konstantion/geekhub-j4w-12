package com.konstantion.dto.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDto(
        UUID id,
        Boolean active,
        List<UUID> productsId,
        UUID tableId,
        UUID userId,
        UUID billId,
        LocalDateTime createdAt,
        LocalDateTime closedAt
) {
}
