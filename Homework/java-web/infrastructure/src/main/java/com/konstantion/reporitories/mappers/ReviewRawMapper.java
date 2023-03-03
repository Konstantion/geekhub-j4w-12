package com.konstantion.reporitories.mappers;

import com.konstantion.review.Review;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public record ReviewRawMapper() implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .id(rs.getLong("id"))
                .uuid(rs.getObject("uuid", UUID.class))
                .message(rs.getString("message"))
                .rating(rs.getDouble("rating"))
                .userUuid(rs.getObject("user_uuid", UUID.class))
                .productUuid(rs.getObject("product_uuid", UUID.class))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}
