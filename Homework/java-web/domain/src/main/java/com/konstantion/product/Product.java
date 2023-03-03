package com.konstantion.product;

import com.konstantion.review.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record Product(Long id, UUID uuid, String name, Double price, String imagePath, LocalDateTime createdAt,
                      UUID userUuid) {

    public Product setId(Long newId) {
        return new Product(newId, uuid, name, price, imagePath, createdAt, userUuid);
    }

    public Product setCreatedAt(LocalDateTime now) {
        return new Product(id, uuid, name, price, imagePath, now, userUuid);
    }

    public Product setUuid(UUID uuid) {
        return new Product(id, uuid, name, price, imagePath, createdAt, userUuid);
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static final class ProductBuilder {
        private Long id;
        private UUID uuid;
        private String name;
        private Double price;

        private String imagePath;
        private LocalDateTime createdAt;
        private UUID userUuid;

        private ProductBuilder() {
        }

        public ProductBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder imagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public ProductBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public ProductBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProductBuilder userUuid(UUID userUuid) {
            this.userUuid = userUuid;
            return this;
        }

        public Product build() {
            return new Product(id, uuid, name, price, imagePath, createdAt, userUuid);
        }
    }
}
