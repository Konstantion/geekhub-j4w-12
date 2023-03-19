package com.konstantion.controllers;

import com.konstantion.response.Response;
import com.konstantion.review.ReviewService;
import com.konstantion.review.dto.CreationReviewDto;
import com.konstantion.review.dto.ReviewDto;
import com.konstantion.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/web-api/reviews")
public record ReviewController(
        ReviewService reviewService,
        User user
) {
    @GetMapping("/products/{uuid}")
    public ResponseEntity<Response> getProductReviews(
            @PathVariable("uuid") UUID uuid
    ) {
        List<UUID> reviewUuids = reviewService.getProductReviews(uuid).stream()
                .map(ReviewDto::uuid).toList();

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Product reviews")
                .data(Map.of("uuids", reviewUuids))
                .build());
    }

    @GetMapping("/products/{uuid}/rating")
    public ResponseEntity<Response> getProductRating(
            @PathVariable("uuid") UUID uuid
    ) {
        Double productRating = reviewService.getProductRating(uuid);

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Product reviews")
                .data(Map.of("rating", productRating))
                .build());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Response> getReview(
            @PathVariable("uuid") UUID uuid
    ) {
        ReviewDto dto = reviewService.getReviewById(uuid);

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Product reviews")
                .data(Map.of("review", dto))
                .build());
    }

    @GetMapping("/users/{uuid}")
    public ResponseEntity<Response> getUserReview(
            @PathVariable("uuid") UUID uuid
    ) {
        List<UUID> uuids = reviewService.getUserReviews(uuid).stream()
                .map(ReviewDto::uuid).toList();

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Users reviews")
                .data(Map.of("uuids", uuids))
                .build());
    }

    @GetMapping("/users")
    public ResponseEntity<Response> getCurrentUserReview() {
        List<UUID> uuids = reviewService.getUserReviews(user.getId()).stream()
                .map(ReviewDto::uuid).toList();

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Users reviews")
                .data(Map.of("uuids", uuids))
                .build());
    }

    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Response> createReview(
            @RequestBody CreationReviewDto creationReviewDto
    ) {
        ReviewDto dto = reviewService.createReview(creationReviewDto, user);

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Review successfully created")
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Response> deleteReview(
            @PathVariable("uuid") UUID uuid
    ) {
        ReviewDto dto = reviewService.deleteReview(uuid, user);

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Review successfully deleted")
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }
}
