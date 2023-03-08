package com.konstantion.review;

import java.time.LocalDateTime;
import java.util.UUID;

public record Review(UUID uuid, String message, Double rating, UUID userUuid, UUID productUuid,
                     LocalDateTime createdAt) {

    public static ReviewBuilder builder() {
        return new ReviewBuilder();
    }

    public static final class ReviewBuilder {
        private UUID uuid;
        private String message;
        private Double rating;
        private UUID userUuid;
        private UUID productUuid;
        private LocalDateTime createdAt;

        private ReviewBuilder() {
        }

        public ReviewBuilder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public ReviewBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ReviewBuilder rating(Double rating) {
            this.rating = rating;
            return this;
        }

        public ReviewBuilder userUuid(UUID userUuid) {
            this.userUuid = userUuid;
            return this;
        }

        public ReviewBuilder productUuid(UUID productUuid) {
            this.productUuid = productUuid;
            return this;
        }

        public ReviewBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Review build() {
            return new Review(uuid, message, rating, userUuid, productUuid, createdAt);
        }
    }
}
