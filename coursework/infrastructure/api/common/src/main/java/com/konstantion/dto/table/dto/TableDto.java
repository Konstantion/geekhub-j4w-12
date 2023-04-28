package com.konstantion.dto.table.dto;

import com.konstantion.table.TableType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record TableDto(
        UUID id,
        String name,
        Integer capacity,
        TableType tableType,
        UUID hallId,
        UUID orderId,
        Set<UUID> waitersId,
        LocalDateTime createdAt,
        LocalDateTime deletedAt,
        Boolean active
) {
}
