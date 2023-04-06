package com.konstantion.table.model;

import java.util.UUID;

public record CreateTableRequest(
        String name,
        Integer capacity,
        String tableType,
        UUID hallUuid,
        String password,
        String username
) {
}
