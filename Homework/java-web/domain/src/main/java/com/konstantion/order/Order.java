package com.konstantion.order;

import com.konstantion.product.Product;
import com.konstantion.user.User;

import java.time.LocalDateTime;
import java.util.Map;

public record Order(Long id, User customer, Map<Product, Integer> products, Integer totalPrice, LocalDateTime placedAt) {

    public Order setId(Long id) {
        return new Order(id, customer, products, totalPrice, placedAt);
    }
    public static OrderBuilder builder() {
        return new OrderBuilder();
    }
    public static final class OrderBuilder {
        private Long id;
        private User customer;
        private Map<Product, Integer> products;
        private Integer totalPrice;
        private LocalDateTime placedAt;

        private OrderBuilder() {
        }

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder customer(User customer) {
            this.customer = customer;
            return this;
        }

        public OrderBuilder products(Map<Product, Integer> products) {
            this.products = products;
            return this;
        }

        public OrderBuilder totalPrice(Integer totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public OrderBuilder placedAt(LocalDateTime placedAt) {
            this.placedAt = placedAt;
            return this;
        }

        public Order build() {
            return new Order(id, customer, products, totalPrice, placedAt);
        }
    }
}
