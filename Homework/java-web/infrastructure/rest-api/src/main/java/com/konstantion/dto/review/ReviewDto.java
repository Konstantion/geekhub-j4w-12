package com.konstantion.dto.review;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewDto(UUID uuid, String message, Double rating, UUID userUuid, UUID productUuid, LocalDateTime createdAt) {
}
