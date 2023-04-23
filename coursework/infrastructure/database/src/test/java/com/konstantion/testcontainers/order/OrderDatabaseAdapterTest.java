package com.konstantion.testcontainers.order;


import com.google.common.collect.Lists;
import com.konstantion.TestApplication;
import com.konstantion.adapters.order.OrderDatabaseAdapter;
import com.konstantion.adapters.product.ProductDatabaseAdapter;
import com.konstantion.config.RowMappersConfiguration;
import com.konstantion.order.Order;
import com.konstantion.product.Product;
import com.konstantion.testcontainers.configuration.DatabaseContainer;
import com.konstantion.testcontainers.configuration.DatabaseTestConfiguration;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfiguration.class, RowMappersConfiguration.class, TestApplication.class})
@Testcontainers
class OrderDatabaseAdapterTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer = DatabaseContainer.getInstance();
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    RowMappersConfiguration rowMappers;

    OrderDatabaseAdapter orderAdapter;
    ProductDatabaseAdapter productAdapter;
    UUID[] PRODUCT_IDS;

    @BeforeEach
    public void setUp() {
        orderAdapter = new OrderDatabaseAdapter(jdbcTemplate, rowMappers.orderRowMapper());

        //Initialize related entities for tests
        productAdapter = new ProductDatabaseAdapter(jdbcTemplate, rowMappers.productRowMapper());
        Product first = Product.builder()
                .name("first")
                .active(true)
                .price(10.0)
                .build();

        Product second = Product.builder()
                .name("second")
                .active(true)
                .price(20.0)
                .build();
        productAdapter.save(first);
        productAdapter.save(second);

        PRODUCT_IDS = new UUID[]{first.getId(), second.getId()};
    }

    @Test
    void shouldReturnOrderWithIdWhenSaveOrderWithoutId() {
        Order order = Order.builder()
                .id(null)
                .active(true)
                .productsId(List.of(PRODUCT_IDS[0], PRODUCT_IDS[1]))
                .createdAt(now())
                .build();

        orderAdapter.save(order);

        assertThat(order.getId())
                .isNotNull();
    }

    @Test
    void shouldReturnOrderWhenFindById() {
        Order order = Order.builder()
                .id(null)
                .active(true)
                .productsId(List.of(PRODUCT_IDS[0], PRODUCT_IDS[1]))
                .createdAt(now())
                .build();
        orderAdapter.save(order);

        Optional<Order> dbOrder = orderAdapter.findById(order.getId());

        assertThat(dbOrder).isPresent()
                .get()
                .matches(matched -> matched.getId().equals(order.getId())
                                    && matched.getProductsId().equals(order.getProductsId()));

        assertThat(dbOrder.get().getProductsId())
                .containsExactlyInAnyOrder(PRODUCT_IDS);
    }

    @Test
    void shouldReturnUpdatedOrderWhenSaveOrderWithId() {
        Order order = Order.builder()
                .id(null)
                .active(true)
                .productsId(Lists.newArrayList(PRODUCT_IDS[0], PRODUCT_IDS[1]))
                .createdAt(now())
                .build();
        orderAdapter.save(order);

        order.setActive(false);
        order.getProductsId().remove(PRODUCT_IDS[1]);
        orderAdapter.save(order);

        Optional<Order> dbOrder = orderAdapter.findById(order.getId());

        assertThat(dbOrder).isPresent()
                .get()
                .matches(matched -> matched.getId().equals(order.getId())
                                    && !matched.isActive()
                                    && matched.getProductsId().equals(order.getProductsId()));
        assertThat(dbOrder.get().getProductsId())
                .containsExactlyInAnyOrder(PRODUCT_IDS[0]);
    }

    @Test
    void shouldDeleteOrderWhenDeleteOrder() {
        Order order = Order.builder()
                .id(null)
                .active(true)
                .productsId(Lists.newArrayList(PRODUCT_IDS[0], PRODUCT_IDS[1]))
                .createdAt(now())
                .build();
        orderAdapter.save(order);

        orderAdapter.delete(order);
        List<Order> orders = orderAdapter.findAll();

        assertThat(orders)
                .isEmpty();
    }

    @Test
    void shouldReturnOrdersWhenFindAll() {
        LocalDateTime createdAt = LocalDateTime.of(2000, 2, 20, 2, 2);
        Order first = Order.builder()
                .id(null)
                .active(true)
                .productsId(Lists.newArrayList(PRODUCT_IDS[0], PRODUCT_IDS[1]))
                .createdAt(createdAt)
                .build();
        Order second = Order.builder()
                .id(null)
                .active(true)
                .productsId(Lists.newArrayList(PRODUCT_IDS[0], PRODUCT_IDS[1]))
                .createdAt(createdAt)
                .build();
        orderAdapter.save(first);
        orderAdapter.save(second);

        List<Order> dbOrders = orderAdapter.findAll();

        assertThat(dbOrders)
                .containsExactlyInAnyOrder(first, second);
    }
}
