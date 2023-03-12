package com.konstantion.reporitories.mappers;

import com.konstantion.order.Order;
import com.konstantion.order.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Component
public record OrderRawMapper() implements RowMapper<Order> {
    private static final Logger logger = LoggerFactory.getLogger(OrderRawMapper.class);

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Order.builder()
                .uuid(rs.getObject("uuid", UUID.class))
                .totalPrice(rs.getDouble("total_price"))
                .placedAt(rs.getTimestamp("placed_at").toLocalDateTime())
                .userUuid(rs.getObject("user_uuid", UUID.class))
                .status(extractStatus(rs))
                .build();
    }

    private OrderStatus extractStatus(ResultSet rs) throws SQLException {
        String statusString = rs.getString("status");
        OrderStatus status = null;
        try {
            if (nonNull(statusString)) {
                status = OrderStatus.valueOf(statusString);
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        }

        return status;
    }
}
