package com.konstantion.testcontainers.call;

import com.konstantion.TestApplication;
import com.konstantion.adapters.bill.BillDatabaseAdapter;
import com.konstantion.bill.Bill;
import com.konstantion.config.RowMappersConfiguration;
import com.konstantion.testcontainers.DatabaseTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfiguration.class, RowMappersConfiguration.class, TestApplication.class})
@Testcontainers
public class CallDatabaseAdapterTest {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    RowMapper<Bill> billRowMapper;

    private BillDatabaseAdapter billAdapter;

    @BeforeEach
    public void setUp() {
        billAdapter = new BillDatabaseAdapter(jdbcTemplate, billRowMapper);
    }
}
