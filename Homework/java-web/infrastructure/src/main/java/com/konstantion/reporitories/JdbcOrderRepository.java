package com.konstantion.reporitories;

import com.konstantion.order.Order;
import com.konstantion.order.OrderRepository;
import com.konstantion.product.Product;
import com.konstantion.utils.ParameterSourceUtil;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public record JdbcOrderRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                  ParameterSourceUtil parameterUtil
) implements OrderRepository {
    private static final String INSERT_ORDER_QUERY = """
                INSERT INTO public.order (uuid, total_price, placed_at, user_uuid)
                VALUES (:uuid, :totalPrice, :placedAt, :userUuid);
            """;
    private static final String INSERT_ORDER_PRODUCT_QUERY = """
                INSERT INTO order_product (order_id, product_id, quantity)
                VALUES (:orderId, :productId, :quantity);
            """;

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameters = parameterUtil.toParameterSource(order);

        jdbcTemplate.update(INSERT_ORDER_QUERY, parameters, keyHolder);

        Long orderId = (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        order = order.setId(orderId);

        saveProducts(order);

        return order;
    }

    @Override
    public void delete(Order object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    private void saveProducts(Order order) {
        Map<Product, Integer> products = order.products();
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            SqlParameterSource parameters = parameterUtil
                    .toParameterSource(order, entry.getKey(), entry.getValue());
            jdbcTemplate.update(INSERT_ORDER_PRODUCT_QUERY, parameters);
        }
    }
}
