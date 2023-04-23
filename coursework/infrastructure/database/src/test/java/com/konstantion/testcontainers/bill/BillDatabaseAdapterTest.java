package com.konstantion.testcontainers.bill;

import com.konstantion.TestApplication;
import com.konstantion.adapters.bill.BillDatabaseAdapter;
import com.konstantion.bill.Bill;
import com.konstantion.testcontainers.DatabaseContainer;
import com.konstantion.testcontainers.DatabaseTestConfiguration;
import com.konstantion.testcontainers.RowMappersTestConfiguration;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.konstantion.testcontainers.MigrationConstants.ORDER_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfiguration.class, RowMappersTestConfiguration.class, TestApplication.class})
@Testcontainers
class BillDatabaseAdapterTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer = DatabaseContainer.getInstance();

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    RowMapper<Bill> billRowMapper;

    private BillDatabaseAdapter billAdapter;

    @BeforeEach
    public void setUp() {
        billAdapter = new BillDatabaseAdapter(jdbcTemplate, billRowMapper);
    }

    @Test
    void shouldReturnBillWithIdWhenSaveBillWithoutId() {
        Bill bill = Bill.builder()
                .id(null)
                .orderId(ORDER_ID)
                .active(true)
                .price(10.00)
                .priceWithDiscount(10.00)
                .build();
        billAdapter.save(bill);

        assertThat(bill).isNotNull()
                .extracting(Bill::getId).isNotNull();
    }


}
