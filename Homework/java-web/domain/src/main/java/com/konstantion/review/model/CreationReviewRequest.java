package com.konstantion.review.model;

import java.util.UUID;

public record CreationReviewRequest(String message, Double rating, UUID productUuid) {
}
