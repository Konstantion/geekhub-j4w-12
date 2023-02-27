package com.konstantion.review.dto;

import java.util.UUID;

public record CreationReviewDto(String message, Integer rating, UUID productUuid) {
}
