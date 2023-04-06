package com.konstantion.controllers;

import com.konstantion.dto.mappers.ReviewMapper;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.dto.review.CreationReviewDto;
import com.konstantion.dto.review.ReviewDto;
import com.konstantion.review.Review;
import com.konstantion.review.ReviewService;
import com.konstantion.review.model.CreationReviewRequest;
import com.konstantion.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/reviews")
public record ReviewController(
        ReviewService reviewService
) {
    public static final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;
    @GetMapping("/products/{uuid}")
    public ResponseEntity<ResponseDto> getProductReviews(
            @PathVariable("uuid") UUID uuid
    ) {
        List<UUID> reviewUuids = reviewService.getProductReviews(uuid).stream()
                .map(Review::uuid).toList();

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Product reviews")
                .data(Map.of("uuids", reviewUuids))
                .build());
    }

    @GetMapping("/products/{uuid}/rating")
    public ResponseEntity<ResponseDto> getProductRating(
            @PathVariable("uuid") UUID uuid
    ) {
        Double productRating = reviewService.getProductRating(uuid);

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Product reviews")
                .data(Map.of("rating", productRating))
                .build());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto> getReview(
            @PathVariable("uuid") UUID uuid
    ) {
        ReviewDto dto =reviewMapper.toDto(reviewService.getReviewById(uuid));

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Product reviews")
                .data(Map.of("review", dto))
                .build());
    }

    @GetMapping("/users/{uuid}")
    public ResponseEntity<ResponseDto> getUserReview(
            @PathVariable("uuid") UUID uuid,
            @AuthenticationPrincipal User user
    ) {
        List<UUID> uuids = reviewService.getUserReviews(uuid, user).stream()
                .map(Review::uuid).toList();

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Users reviews")
                .data(Map.of("uuids", uuids))
                .build());
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createReview(
            @RequestBody CreationReviewDto creationReviewDto,
            @AuthenticationPrincipal User user
    ) {
        ReviewDto dto = reviewMapper.toDto(
                reviewService.createReview(reviewMapper.toEntity(creationReviewDto), user)
        );

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Review successfully created")
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ResponseDto> deleteReview(
            @PathVariable("uuid") UUID uuid,
            @AuthenticationPrincipal User user
    ) {
        ReviewDto dto = reviewMapper.toDto(reviewService.deleteReview(uuid, user));

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Review successfully deleted")
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }
}
