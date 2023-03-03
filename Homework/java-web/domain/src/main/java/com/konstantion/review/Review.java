package com.konstantion.review;

import java.time.LocalDateTime;
import java.util.UUID;

public record Review(Long id, UUID uuid, String message, Double rating, UUID userUuid, UUID productUuid,
                     LocalDateTime createdAt) {

    public static ReviewBuilder builder() {
        return new ReviewBuilder();
    }

    public Review setId(Long newId) {
        return new Review(newId, uuid, message, rating, userUuid, productUuid, createdAt);
    }

    public static final class ReviewBuilder {
        private Long id;
        private UUID uuid;
        private String message;
        private Double rating;
        private UUID userUuid;
        private UUID productUuid;
        private LocalDateTime createdAt;

        private ReviewBuilder() {
        }

        public ReviewBuilder id(Long id) {
            this.id = id;
            return this;
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
            return new Review(id, uuid, message, rating, userUuid, productUuid, createdAt);
        }
    }
}
