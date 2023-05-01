package com.konstantion.adapters.product;

import com.konstantion.ApplicationStarter;
import com.konstantion.configuration.RowMappersConfiguration;
import com.konstantion.product.Product;
import com.konstantion.testcontainers.configuration.DatabaseContainer;
import com.konstantion.testcontainers.configuration.DatabaseTestConfiguration;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfiguration.class, RowMappersConfiguration.class, ApplicationStarter.class})
@Testcontainers
@ActiveProfiles("test")
class ProductDatabaseAdapterTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer = DatabaseContainer.getInstance();
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    RowMappersConfiguration rowMappers;

    ProductDatabaseAdapter productAdapter;

    @BeforeEach
    void setUp() {
        productAdapter = new ProductDatabaseAdapter(jdbcTemplate, rowMappers.productRowMapper());
    }

    @AfterEach
    void cleanUp() {
        productAdapter.deleteAll();
    }

    @Test
    void shouldReturnProductWithIdwWhenSaveProductWithoutId() {
        Product product = Product.builder()
                .name("test")
                .price(10.0)
                .active(true)
                .build();

        productAdapter.save(product);

        assertThat(product.getId())
                .isNotNull();
    }

    @Test
    void shouldReturnUpdatedProductWhenSaveProductWithId() {
        Product product = Product.builder()
                .name("test")
                .price(10.0)
                .active(true)
                .build();
        productAdapter.save(product);

        product.setDescription("test product description");
        Product dbProduct = productAdapter.save(product);

        assertThat(dbProduct).isNotNull()
                .isEqualTo(product);
    }

    @Test
    void shouldReturnProductWhenFindById() {
        Product product = Product.builder()
                .name("test")
                .price(10.0)
                .active(true)
                .build();
        productAdapter.save(product);
        UUID id = product.getId();

        Optional<Product> dbProduct = productAdapter.findById(id);

        assertThat(dbProduct).isPresent()
                .get()
                .matches(matched -> matched.getId().equals(id)
                                    && matched.equals(product));
    }

    @Test
    void shouldDeleteProductWhenDelete() {
        Product product = Product.builder()
                .name("test")
                .price(10.0)
                .active(true)
                .build();
        productAdapter.save(product);
        UUID productId = product.getId();

        productAdapter.delete(product);
        Optional<Product> dbProduct = productAdapter.findById(productId);

        assertThat(dbProduct).isNotPresent();
    }

    @Test
    void shouldReturnProductsWhenGetAll() {
        Product first = Product.builder().name("first").active(true).price(10.0).build();
        Product second = Product.builder().name("second").active(true).price(11.0).build();
        Product third = Product.builder().name("third").active(true).price(12.0).build();
        productAdapter.save(first);
        productAdapter.save(second);
        productAdapter.save(third);

        Page<Product> page_1_size_2_orderBy_price = productAdapter.findAll(1, 2, "price", "", null, true, true);
        Page<Product> page_1_size_4_OrderBy_price_searchPattern_i = productAdapter.findAll(1, 4, "price", "i", null, true, true);

        assertThat(page_1_size_2_orderBy_price.getContent())
                .hasSize(2)
                .containsExactly(first, second);

        assertThat(page_1_size_4_OrderBy_price_searchPattern_i)
                .hasSize(2)
                .containsExactly(first, third);
    }
}