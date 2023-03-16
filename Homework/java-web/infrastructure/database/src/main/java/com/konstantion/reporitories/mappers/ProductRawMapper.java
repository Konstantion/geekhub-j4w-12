package com.konstantion.reporitories.mappers;

import com.konstantion.product.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public record ProductRawMapper() implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Product.builder()
                .uuid(rs.getObject("uuid", UUID.class))
                .price(rs.getDouble("price"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .imageBytes(rs.getBytes("image_bytes"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .userUuid(rs.getObject("user_uuid", UUID.class))
                .categoryUuid(rs.getObject("category_uuid", UUID.class))
                .build();
    }
}
