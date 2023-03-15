package com.konstantion.reporitories;

import com.konstantion.order.Order;
import com.konstantion.order.OrderRepository;
import com.konstantion.order.OrderStatus;
import com.konstantion.product.Product;
import com.konstantion.reporitories.mappers.OrderProductsRawMapper;
import com.konstantion.reporitories.mappers.OrderRawMapper;
import com.konstantion.utils.ParameterSourceUtil;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
public record JdbcOrderRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                  ParameterSourceUtil parameterUtil,
                                  OrderRawMapper orderRawMapper,
                                  OrderProductsRawMapper orderProductsRawMapper
) implements OrderRepository {
    private static final String INSERT_ORDER_QUERY = """
                INSERT INTO public.order (uuid, total_price, placed_at, user_uuid, status)
                VALUES (:uuid, :totalPrice, :placedAt, :userUuid, :status);
            """;
    private static final String INSERT_ORDER_PRODUCT_QUERY = """
                INSERT INTO order_product (order_uuid, product_uuid, quantity)
                VALUES (:orderUuid, :productUuid, :quantity);
            """;

    private static final String FIND_ALL_ORDERS_QUERY = """
                SELECT * FROM public.order;
            """;

    private static final String FIND_ORDER_BY_ID_QUERY = """
                SELECT * FROM public.order
                WHERE uuid = :uuid;
            """;

    private static final String FIND_ORDER_PRODUCTS_BY_ID_QUERY = """
                SELECT  p.*, op.quantity FROM public.order ord
                INNER JOIN order_product op on ord.uuid = op.order_uuid
                INNER JOIN product p on op.product_uuid = p.uuid
                WHERE ord.uuid = :uuid;
            """;

    private static final String FIND_ORDER_BY_USER_ID_QUERY = """
                SELECT * FROM public.order
                WHERE user_uuid = :userUuid;
            """;

    private static final String UPDATE_STATUS_ORDER_BY_ID = """
            UPDATE public.order
            SET status = :status
            WHERE uuid = :uuid;
            """;

    @Override
    public List<Order> findAll() {
        List<Order> orders = jdbcTemplate.query(FIND_ALL_ORDERS_QUERY, orderRawMapper);

        return orders.stream()
                .map(order -> order.setProducts(getProducts(order)))
                .toList();
    }

    @Override
    public List<Order> findByUserId(UUID uuid) {
        List<Order> orders = jdbcTemplate
                .query(FIND_ORDER_BY_USER_ID_QUERY, Map.of("userUuid", uuid), orderRawMapper);

        return orders.stream()
                .map(order -> order.setProducts(getProducts(order)))
                .toList();
    }

    @Override
    public Optional<Order> findById(UUID uuid) {
        Order order = jdbcTemplate
                .query(FIND_ORDER_BY_ID_QUERY, Map.of("uuid", uuid), orderRawMapper).get(0);
        if (nonNull(order)) {
            Map<Product, Integer> products = getProducts(order);
            order = order.setProducts(products);
        }

        return Optional.ofNullable(order);
    }

    @Override
    public Order save(Order order) {
        SqlParameterSource parameters = parameterUtil.toParameterSource(order);
        jdbcTemplate.update(INSERT_ORDER_QUERY, parameters);
        saveProducts(order);
        return order;
    }

    @Override
    public void updateOrderStatusById(UUID uuid, OrderStatus status) {
        jdbcTemplate.update(
                UPDATE_STATUS_ORDER_BY_ID,
                Map.of("status", status.name(), "uuid", uuid)
        );
    }

    @Override
    public OrderStatus findStatusById(UUID uuid) {
        return null;
    }

    private void saveProducts(Order order) {
        Map<Product, Integer> products = order.products();
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            SqlParameterSource parameters = parameterUtil
                    .toParameterSource(order, entry.getKey(), entry.getValue());
            jdbcTemplate.update(INSERT_ORDER_PRODUCT_QUERY, parameters);
        }
    }

    private Map<Product, Integer> getProducts(Order order) {
        List<Map.Entry<Product, Integer>> productsList = jdbcTemplate
                .query(FIND_ORDER_PRODUCTS_BY_ID_QUERY, Map.of("uuid", order.uuid()), orderProductsRawMapper);

        return productsList.stream().collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue
        ));
    }
}
