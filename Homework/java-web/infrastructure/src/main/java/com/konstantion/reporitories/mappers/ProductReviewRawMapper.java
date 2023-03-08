package com.konstantion.reporitories.mappers;

import com.konstantion.product.Product;
import com.konstantion.review.Review;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@Component
public record ProductReviewRawMapper()
        implements RowMapper<Map.Entry<Product, Review>> {
    @Override
    public Map.Entry<Product, Review> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = Product.builder()
                .uuid(rs.getObject(1, UUID.class))
                .name(rs.getString(2))
                .price(rs.getDouble(3))
                .createdAt(rs.getTimestamp(4) != null ? rs.getTimestamp(4).toLocalDateTime() : null)
                .userUuid(rs.getObject(5, UUID.class))
                .imagePath(rs.getString(6))
                .description(rs.getString(7))
                .category(rs.getString(8))
                .build();

        Review review = Review.builder()
                .uuid(rs.getObject(9, UUID.class))
                .message(rs.getString(10))
                .rating(rs.getDouble(11))
                .userUuid(rs.getObject(12, UUID.class))
                .productUuid(rs.getObject(13, UUID.class))
                .createdAt(rs.getTimestamp(14) != null ? rs.getTimestamp(14).toLocalDateTime() : null)
                .build();
        return Map.entry(product, review);
    }
}
