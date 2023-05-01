package com.konstantion.adapters.bill;

import com.konstantion.ApplicationStarter;
import com.konstantion.adapters.order.OrderDatabaseAdapter;
import com.konstantion.bill.Bill;
import com.konstantion.configuration.RowMappersConfiguration;
import com.konstantion.order.Order;
import com.konstantion.testcontainers.configuration.DatabaseContainer;
import com.konstantion.testcontainers.configuration.DatabaseTestConfiguration;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfiguration.class, RowMappersConfiguration.class, ApplicationStarter.class})
@Testcontainers
@ActiveProfiles("test")
class BillDatabaseAdapterTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer = DatabaseContainer.getInstance();

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    RowMapper<Bill> billRowMapper;

    @Autowired
    RowMapper<Order> orderRowMapper;

    BillDatabaseAdapter billAdapter;
    OrderDatabaseAdapter orderDatabaseAdapter;
    UUID[] ORDER_IDS;

    @BeforeEach
    void setUp() {
        billAdapter = new BillDatabaseAdapter(jdbcTemplate, billRowMapper);
        //Initialize related entities for tests
        orderDatabaseAdapter = new OrderDatabaseAdapter(jdbcTemplate, orderRowMapper);
        Order first = Order.builder()
                .createdAt(now())
                .active(true)
                .build();
        Order second = Order.builder()
                .createdAt(now())
                .active(true)
                .build();
        orderDatabaseAdapter.save(first);
        orderDatabaseAdapter.save(second);

        ORDER_IDS = new UUID[]{first.getId(), second.getId()};
    }
    @AfterEach
    void cleanUp() {
        billAdapter.deleteAll();
        orderDatabaseAdapter.deleteAll();
    }

    @Test
    void shouldReturnBillWithIdWhenSaveBillWithoutId() {
        Bill bill = Bill.builder()
                .id(null)
                .orderId(ORDER_IDS[0])
                .active(true)
                .price(10.00)
                .priceWithDiscount(10.00)
                .build();
        billAdapter.save(bill);

        assertThat(bill).isNotNull()
                .extracting(Bill::getId).isNotNull();
    }

    @Test
    void shouldReturnSavedBillFindById() {
        Bill bill = Bill.builder()
                .id(null)
                .orderId(ORDER_IDS[0])
                .active(true)
                .price(10.00)
                .priceWithDiscount(10.00)
                .build();
        billAdapter.save(bill);

        Optional<Bill> dbBill = billAdapter.findById(bill.getId());

        assertThat(dbBill).isPresent()
                .get().isEqualTo(bill);
    }

    @Test
    void shouldReturnAllBillsWhenGetAll() {
        Bill first = Bill.builder()
                .id(null)
                .orderId(ORDER_IDS[0])
                .active(true)
                .price(10.00)
                .priceWithDiscount(10.00)
                .build();
        Bill second = Bill.builder()
                .id(null)
                .orderId(ORDER_IDS[1])
                .active(true)
                .price(23.00)
                .priceWithDiscount(10.00)
                .build();

        billAdapter.save(first);
        billAdapter.save(second);

        List<Bill> dbBills = billAdapter.findAll();

        assertThat(dbBills)
                .hasSize(2)
                .containsExactlyInAnyOrder(first, second);
    }

    @Test
    void shouldDeleteBillWhenDelete() {
        Bill bill = Bill.builder()
                .id(null)
                .orderId(ORDER_IDS[0])
                .active(true)
                .price(10.00)
                .priceWithDiscount(10.00)
                .build();

        billAdapter.save(bill);
        billAdapter.delete(bill);
        List<Bill> dbBills = billAdapter.findAll();

        assertThat(dbBills).isEmpty();
    }

    @Test
    void shouldReturnOptionalWithBillWhenFindByOrderIdWithExistingOrderId() {
        Bill bill = Bill.builder()
                .id(null)
                .orderId(ORDER_IDS[0])
                .active(true)
                .price(10.00)
                .priceWithDiscount(10.00)
                .build();

        billAdapter.save(bill);
        Optional<Bill> dbBill = billAdapter.findByOrderId(ORDER_IDS[0]);

        assertThat(dbBill).isPresent()
                .get().isEqualTo(bill);
    }

    @Test
    void shouldReturnOptionalEmptyWhenFindByOrderIdWithNonExistingOrderId() {
        Bill bill = Bill.builder()
                .id(null)
                .orderId(ORDER_IDS[0])
                .active(true)
                .price(10.00)
                .priceWithDiscount(10.00)
                .build();

        billAdapter.save(bill);
        Optional<Bill> dbBill = billAdapter.findByOrderId(ORDER_IDS[1]);

        assertThat(dbBill).isNotPresent();
    }

    @Test
    void shouldUpdateBillWhenSaveBillWithBillThatExists() {
        Bill bill = Bill.builder()
                .id(null)
                .orderId(ORDER_IDS[0])
                .active(true)
                .price(10.00)
                .priceWithDiscount(10.00)
                .build();
        billAdapter.save(bill);

        UUID dbId = bill.getId();
        Bill newBill = Bill.builder()
                .id(dbId)
                .orderId(ORDER_IDS[0])
                .active(true)
                .price(123.00)
                .priceWithDiscount(10.00)
                .build();
        billAdapter.save(newBill);

        Optional<Bill> dbBill = billAdapter.findById(dbId);

        assertThat(dbBill).isPresent()
                .get()
                .matches(matched -> matched.getPrice().equals(123.00)
                                    && matched.getId().equals(bill.getId()));
    }
}
