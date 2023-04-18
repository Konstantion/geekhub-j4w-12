package com.konstantion.dto.table.dto;

import java.util.UUID;

public record CreateTableRequestDto(
        String name,
        Integer capacity,
        String tableType,
        UUID hallUuid,
        String password
) {
}
