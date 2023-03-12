package com.konstantion.order;

import com.konstantion.product.Product;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record Order(UUID uuid, UUID userUuid,
                    Map<Product, Integer> products, Double totalPrice,
                    LocalDateTime placedAt, OrderStatus status
) {

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public Order setUuid(UUID uuid) {
        return new Order(uuid, userUuid, products, totalPrice, placedAt, status);
    }

    public Order setProducts(Map<Product, Integer> products) {
        return new Order(uuid, userUuid, products, totalPrice, placedAt, status);
    }

    public static final class OrderBuilder {
        private UUID uuid;
        private Map<Product, Integer> products;
        private Double totalPrice;
        private LocalDateTime placedAt;
        private UUID userUuid;

        private OrderStatus status;

        private OrderBuilder() {
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

        public OrderBuilder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Order build() {
            return new Order(uuid, userUuid, products, totalPrice, placedAt, status);
        }
    }
}
