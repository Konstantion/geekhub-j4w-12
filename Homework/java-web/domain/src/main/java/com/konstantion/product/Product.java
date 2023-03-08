package com.konstantion.product;

import com.konstantion.category.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record Product(UUID uuid, String name, Double price, String description, String category, String imagePath,
                      LocalDateTime createdAt,
                      UUID userUuid) {

    public Product setCreatedAt(LocalDateTime now) {
        return new Product(uuid, name, price, description, category, imagePath, now, userUuid);
    }

    public Product setImagePath(String imagePath) {
        return new Product(uuid, name, price, description, category, imagePath, createdAt, userUuid);
    }

    public Product setUuid(UUID uuid) {
        return new Product(uuid, name, price, description, category, imagePath, createdAt, userUuid);
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static final class ProductBuilder {
        private UUID uuid;
        private String name;
        private Double price;

        private String description;
        private String category;

        private String imagePath;
        private LocalDateTime createdAt;
        private UUID userUuid;

        private ProductBuilder() {
        }

        public ProductBuilder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder category(String category) {
            this.category = category;
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
            return new Product(uuid, name, price, description, category, imagePath, createdAt, userUuid);
        }
    }
}
