package com.konstantion.utils;

import com.konstantion.order.Order;
import com.konstantion.product.Product;
import com.konstantion.review.Review;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public record ParameterSourceUtil() {
    public MapSqlParameterSource toParameterSourceWithoutId(Product product) {
        return new MapSqlParameterSource()
                .addValue("uuid", product.uuid())
                .addValue("name", product.name())
                .addValue("price", product.price())
                .addValue("createdAt", product.createdAt())
                .addValue("userUuid", product.userUuid());
    }

    public MapSqlParameterSource toParameterSource(Product product) {
        return toParameterSourceWithoutId(product)
                .addValue("id", product.id());
    }

    public MapSqlParameterSource toParameterSourceWithoutId(Order order) {
        return new MapSqlParameterSource()
                .addValue("uuid", order.uuid())
                .addValue("totalPrice", order.totalPrice())
                .addValue("products", order.products())
                .addValue("placedAt", order.placedAt())
                .addValue("userUuid", order.userUuid());
    }

    public MapSqlParameterSource toParameterSource(Order order) {
        return toParameterSourceWithoutId(order).addValue("id", order.id());
    }

    public MapSqlParameterSource toParameterSource(Order order, Product product, Integer quantity) {
        return new MapSqlParameterSource()
                .addValue("orderId", order.id())
                .addValue("productId", product.id())
                .addValue("quantity", quantity);
    }

    public MapSqlParameterSource toParameterSourceWithoutId(Review review) {
        return new MapSqlParameterSource()
                .addValue("uuid", review.uuid())
                .addValue("message", review.message())
                .addValue("rating", review.rating())
                .addValue("productUuid", review.productUuid())
                .addValue("userUuid", review.userUuid())
                .addValue("createdAt", review.createdAt());
    }
}
