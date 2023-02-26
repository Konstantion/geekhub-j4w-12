package com.konstantion.order;

import com.konstantion.product.Product;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record Order(Long id, UUID uuid, UUID userUuid, Map<Product, Integer> products, Double totalPrice, LocalDateTime placedAt) {

    public Order setId(Long id) {
        return new Order(id, uuid, userUuid, products, totalPrice, placedAt);
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static final class OrderBuilder {
        private Long id;
        private UUID uuid;
        private Map<Product, Integer> products;
        private Double totalPrice;
        private LocalDateTime placedAt;
        private UUID userUuid;

        private OrderBuilder() {
        }

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public OrderBuilder userUuid(UUID userUuid) {
            this.userUuid = userUuid;
            return this;
        }

        public OrderBuilder products(Map<Product, Integer> products) {
            this.products = products;
            return this;
        }

        public OrderBuilder totalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public OrderBuilder placedAt(LocalDateTime placedAt) {
            this.placedAt = placedAt;
            return this;
        }

        public Order build() {
            return new Order(id, uuid, userUuid, products, totalPrice, placedAt);
        }
    }
}
