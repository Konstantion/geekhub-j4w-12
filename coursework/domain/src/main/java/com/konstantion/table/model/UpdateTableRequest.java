package com.konstantion.table.model;

import java.util.UUID;

public record UpdateTableRequest(
        String name,
        Integer capacity,
        String tableType,
        UUID hallId,
        String password
) {
}
