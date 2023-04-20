package com.konstantion.adapters.order;

import com.konstantion.order.Order;
import com.konstantion.order.OrderPort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Repository
public class OrderDatabaseAdapter implements OrderPort {
    final NamedParameterJdbcTemplate jdbcTemplate;
    final RowMapper<Order> orderRowMapper;

    public OrderDatabaseAdapter(NamedParameterJdbcTemplate jdbcTemplate, RowMapper<Order> orderRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderRowMapper = orderRowMapper;
    }

    private static final String FIND_BY_ID_QUERY = """
            SELECT * FROM public.order
            WHERE id = :id;
            """;

    private static final String FIND_PRODUCTS_BY_ORDER_ID = """
            SELECT product_id FROM public.order_product
            WHERE order_id = :orderId;
            """;

    private static final String DELETE_QUERY = """
            DELETE FROM public.order
            WHERE id = :id;
            """;

    private static final String UPDATE_QUERY = """
            UPDATE public.order
            SET table_id = :tableId,
                user_id = :userId,
                bill_id = :billId,
                created_at = :createdAt,
                closed_at = :closedAt,
                active = :active
            WHERE id = :id;
            """;

    private static final String DELETE_PRODUCTS_QUERY = """
            DELETE FROM public.order_product
            WHERE order_id = :orderId;
            """;

    private static final String SAVE_PRODUCT_QUERY = """
            INSERT INTO public.order_product (order_id, product_id)
            VALUES (:orderId, :productId);
            """;

    private static final String SAVE_QUERY = """
            INSERT INTO public.order (table_id, user_id, bill_id, created_at, closed_at, active)
            VALUES (:tableId, :userId, :billId, :createdAt, :closedAt, :active);
            """;

    private static final String FIND_ALL_QUERY = """
            SELECT * FROM public.order;
            """;

    @Override
    public Order save(Order order) {
        if (nonNull(order.getId())) {
            return update(order);
        }

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(order);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                SAVE_QUERY,
                parameterSource,
                keyHolder
        );

        UUID generatedId = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        order.setId(generatedId);

        updateOrderProducts(order);

        return order;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        Order order = jdbcTemplate.query(
                FIND_BY_ID_QUERY,
                parameterSource,
                orderRowMapper
        ).stream().findFirst().orElse(null);

        if (nonNull(order)) {
            order.setProductsId(findProductsByOrderId(order.getId()));
        }

        return Optional.ofNullable(order);
    }

    @Override
    public void delete(Order order) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", order.getId());

        jdbcTemplate.update(
                DELETE_QUERY,
                parameterSource
        );
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = jdbcTemplate.query(
                FIND_ALL_QUERY,
                orderRowMapper
        );

        for (Order order : orders) {
            order.setProductsId(findProductsByOrderId(order.getId()));
        }

        return orders;
    }

    private List<UUID> findProductsByOrderId(UUID orderId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("orderId", orderId);

        return jdbcTemplate.queryForList(
                FIND_PRODUCTS_BY_ORDER_ID,
                parameterSource,
                UUID.class
        );
    }

    private Order update(Order order) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(order);
        jdbcTemplate.update(
                UPDATE_QUERY,
                parameterSource
        );

        updateOrderProducts(order);

        return order;
    }

    private void updateOrderProducts(Order order) {
        deleteOrderProducts(order.getId());
        saveOrderProducts(order.getId(), order.getProductsId());
    }

    private void deleteOrderProducts(UUID orderId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("orderId", orderId);
        jdbcTemplate.update(
                DELETE_PRODUCTS_QUERY,
                parameterSource
        );
    }

    private void saveOrderProducts(UUID orderId, List<UUID> productsId) {
        productsId.forEach(productId -> {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("orderId", orderId)
                    .addValue("productId", productId);
            jdbcTemplate.update(
                    SAVE_PRODUCT_QUERY,
                    parameterSource
            );
        });
    }
}
