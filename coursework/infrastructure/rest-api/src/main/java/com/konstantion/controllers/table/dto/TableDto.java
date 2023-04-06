package com.konstantion.controllers.table.dto;

import com.konstantion.table.TableType;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

public record TableDto(
        UUID id,
        String name,
        Integer capacity,
        TableType tableType,
        UUID hallId,
        UUID orderId,
        List<UUID> waitersId,
        Boolean active
) {
    public boolean hasOrder() {
        return nonNull(orderId);
    }
}
