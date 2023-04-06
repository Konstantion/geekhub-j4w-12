    package com.konstantion.adapters.bill;

    import com.konstantion.bill.Bill;
    import org.junit.jupiter.api.Test;
    import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
    import org.springframework.jdbc.core.namedparam.SqlParameterSource;

    import java.sql.ResultSet;
    import java.util.UUID;

    class BillDatabaseAdapterTest {
        @Test
        void beanPropSource() {
            Bill bill = Bill.builder()
                    .id(UUID.randomUUID())
                    .active(true)
                    .price(123.0)
                    .orderId(UUID.randomUUID())
                    .guestId(UUID.randomUUID())
                    .build();

            SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(bill);
            System.out.println(parameterSource.getParameterNames());
            System.out.println(parameterSource.getValue("id"));
        }
    }