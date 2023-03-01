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
        implements RowMapper<Map.Entry<Product, Review>>{
    @Override
    public Map.Entry<Product, Review> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = Product.builder()
                .id(rs.getLong(1))
                .uuid(rs.getObject(2, UUID.class))
                .name(rs.getString(3))
                .price(rs.getDouble(4))
                .createdAt(rs.getTimestamp(5) != null ? rs.getTimestamp(5).toLocalDateTime() : null)
                .userUuid(rs.getObject(6, UUID.class))
                .build();

        Review review = Review.builder()
                .id(rs.getLong(7))
                .uuid(rs.getObject(8, UUID.class))
                .message(rs.getString(9))
                .rating(rs.getInt(10))
                .userUuid(rs.getObject(11, UUID.class))
                .productUuid(rs.getObject(12, UUID.class))
                .createdAt(rs.getTimestamp(13) != null ? rs.getTimestamp(13).toLocalDateTime() : null)
                .build();
        return Map.entry(product, review);
    }
}
