package com.konstantion.dto.review;

import java.util.UUID;

public record CreationReviewDto(String message, Double rating, UUID productUuid) {
}
