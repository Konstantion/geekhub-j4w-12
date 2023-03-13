package com.konstantion.table.dto;

import java.util.UUID;

public record CreationTableDto(
        String name,
        Integer capacity,
        String tableType,
        UUID hallUuid,
        String password,
        String username
) {
    public CreationTableDto setHallUuid(UUID hallUuid) {
        return new CreationTableDto(name, capacity, tableType, hallUuid, password, username);
    }
}
