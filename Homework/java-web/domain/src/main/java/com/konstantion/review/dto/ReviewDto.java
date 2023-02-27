package com.konstantion.review.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewDto(UUID uuid, String message, Integer rating, UUID userUuid, UUID productUuid, LocalDateTime createdAt) {
}
