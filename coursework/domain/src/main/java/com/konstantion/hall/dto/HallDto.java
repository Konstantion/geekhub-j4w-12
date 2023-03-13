package com.konstantion.hall.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record HallDto(UUID id, String name, LocalDateTime createdAt, Boolean active) {
}
