package com.konstantion.product.dto;

import com.konstantion.review.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProductDto(UUID uuid, String name, Double price, String imagePath, LocalDateTime createdAt, UUID userUuid, List<ReviewDto> reviews, Double rating) {

    public ProductDto setReviews(List<ReviewDto> newReviews) {
        return new ProductDto(uuid, name, price, imagePath,createdAt, userUuid, newReviews, rating);
    }

    public ProductDto setRating(Double newRating) {
        return new ProductDto(uuid, name, price, imagePath, createdAt, userUuid, reviews, newRating);
    }
}
