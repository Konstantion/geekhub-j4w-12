package com.konstantion.product;

import com.google.common.base.Objects;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

public record Product(UUID uuid, String name, Double price,
                      String description, byte[] imageBytes,
                      LocalDateTime createdAt, UUID userUuid) {

    public Product setCreatedAt(LocalDateTime now) {
        return new Product(uuid, name, price, description, imageBytes, now, userUuid);
    }

    public Product setImageBytes(byte[] imageBytes) {
        return new Product(uuid, name, price, description, imageBytes, createdAt, userUuid);
    }

    public Product setUuid(UUID uuid) {
        return new Product(uuid, name, price, description, imageBytes, createdAt, userUuid);
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static final class ProductBuilder {
        private UUID uuid;
        private String name;
        private Double price;
        private String description;
        private byte[] imageBytes;
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

        public ProductBuilder imageBytes(byte[] imageBytes) {
            this.imageBytes = imageBytes;
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
            return new Product(uuid, name, price, description, imageBytes, createdAt, userUuid);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equal(uuid, product.uuid) && Objects.equal(name, product.name) && Objects.equal(price, product.price) && Objects.equal(description, product.description) && Objects.equal(imageBytes, product.imageBytes) && Objects.equal(createdAt, product.createdAt) && Objects.equal(userUuid, product.userUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid, name, price, description, imageBytes, createdAt, userUuid);
    }

    @Override
    public String toString() {
        return "Product{" +
               "uuid=" + uuid +
               ", name='" + name + '\'' +
               ", price=" + price +
               ", description='" + description + '\'' +
               ", imageBytes=" + Arrays.toString(imageBytes) +
               ", createdAt=" + createdAt +
               ", userUuid=" + userUuid +
               '}';
    }
}
