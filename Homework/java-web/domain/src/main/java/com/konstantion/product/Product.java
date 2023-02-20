package com.konstantion.product;

import java.time.LocalDateTime;

public record Product(Long id, String name, Integer price, LocalDateTime createdAt) {

    public Product setId(Long newId) {
        return new Product(newId, name, price, createdAt);
    }

    public Product setCreatedAt(LocalDateTime now) {
        return new Product(id, name, price, now);
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static final class ProductBuilder {
        private Long id;
        private String name;
        private Integer price;
        private LocalDateTime createdAt;

        private ProductBuilder() {
        }

        public ProductBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(Integer price) {
            this.price = price;
            return this;
        }

        public ProductBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Product build() {
            return new Product(id, name, price, createdAt);
        }
    }
}
