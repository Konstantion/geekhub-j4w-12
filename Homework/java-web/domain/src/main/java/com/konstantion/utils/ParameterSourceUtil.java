package com.konstantion.utils;

import com.konstantion.category.Category;
import com.konstantion.order.Order;
import com.konstantion.product.Product;
import com.konstantion.ragistration.token.ConfirmationToken;
import com.konstantion.review.Review;
import com.konstantion.user.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public record ParameterSourceUtil() {
    public MapSqlParameterSource toParameterSource(Product product) {
        return new MapSqlParameterSource()
                .addValue("uuid", product.uuid())
                .addValue("name", product.name())
                .addValue("price", product.price())
                .addValue("description", product.description())
                .addValue("createdAt", product.createdAt())
                .addValue("userUuid", product.userUuid())
                .addValue("imageBytes", product.imageBytes())
                .addValue("categoryUuid", product.categoryUuid());
    }

    public MapSqlParameterSource toParameterSource(Order order) {
        return new MapSqlParameterSource()
                .addValue("uuid", order.uuid())
                .addValue("totalPrice", order.totalPrice())
                .addValue("products", order.products())
                .addValue("placedAt", order.placedAt())
                .addValue("status", order.status().name())
                .addValue("userUuid", order.userUuid());
    }

    public MapSqlParameterSource toParameterSource(Order order, Product product, Integer quantity) {
        return new MapSqlParameterSource()
                .addValue("orderUuid", order.uuid())
                .addValue("productUuid", product.uuid())
                .addValue("quantity", quantity);
    }

    public MapSqlParameterSource toParameterSource(Review review) {
        return new MapSqlParameterSource()
                .addValue("uuid", review.uuid())
                .addValue("message", review.message())
                .addValue("rating", review.rating())
                .addValue("productUuid", review.productUuid())
                .addValue("userUuid", review.userUuid())
                .addValue("createdAt", review.createdAt());
    }

    public MapSqlParameterSource toParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("uuid", user.getId())
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("phoneNumber", user.getPhoneNumber())
                .addValue("password", user.getPassword())
                .addValue("enabled", user.isEnabled())
                .addValue("nonLocked", user.isAccountNonLocked());
    }

    public MapSqlParameterSource toParameterSource(Category category) {
        return new MapSqlParameterSource()
                .addValue("uuid", category.uuid())
                .addValue("name", category.name())
                .addValue("createdAt", category.createdAt())
                .addValue("userUuid", category.userUuid());
    }

    public MapSqlParameterSource toParameterSource(ConfirmationToken token) {
        return new MapSqlParameterSource()
                .addValue("uuid", token.id())
                .addValue("token", token.token())
                .addValue("createdAt", token.createdAt())
                .addValue("expiresAt", token.expiresAt())
                .addValue("confirmedAt", token.confirmedAt())
                .addValue("userUuid", token.userId());
    }
}
