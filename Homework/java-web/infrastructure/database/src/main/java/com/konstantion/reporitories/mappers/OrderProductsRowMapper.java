package com.konstantion.reporitories.mappers;

import com.konstantion.product.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Component
public record OrderProductsRowMapper(
        ProductRowMapper productRawMapper) implements RowMapper<Map.Entry<Product, Integer>> {

    @Override
    public Map.Entry<Product, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = productRawMapper.mapRow(rs, rowNum);
        Integer quantity = rs.getInt("quantity");

        return Map.entry(product, quantity);
    }
}
