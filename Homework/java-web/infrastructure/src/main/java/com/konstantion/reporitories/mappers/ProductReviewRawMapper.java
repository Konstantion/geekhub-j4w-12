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
                .id(rs.getLong("p.id"))
                .uuid(rs.getObject("p.uuid", UUID.class))
                .price(rs.getDouble("p.price"))
                .name(rs.getString("p.name"))
                .createdAt(rs.getTimestamp("p.created_at").toLocalDateTime())
                .userUuid(rs.getObject("p.user_uuid", UUID.class))
                .build();
        Review review = Review.builder()
                .id(rs.getLong("r.id"))
                .uuid(rs.getObject("r.uuid", UUID.class))
                .message(rs.getString("r.message"))
                .rating(rs.getInt("r.rating"))
                .userUuid(rs.getObject("r.user_uuid", UUID.class))
                .productUuid(rs.getObject("r.product_uuid", UUID.class))
                .createdAt(rs.getTimestamp("r.created_at").toLocalDateTime())
                .build();
        return Map.entry(product, review);
    }
}
