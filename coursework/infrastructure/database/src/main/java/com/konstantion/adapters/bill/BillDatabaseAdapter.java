package com.konstantion.adapters.bill;

import com.konstantion.bill.Bill;
import com.konstantion.bill.BillPort;
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
public class BillDatabaseAdapter implements BillPort {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Bill> billRowMapper;

    public BillDatabaseAdapter(NamedParameterJdbcTemplate jdbcTemplate, RowMapper<Bill> billRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.billRowMapper = billRowMapper;
    }

    public static final String FIND_BY_ID_QUERY = """
            SELECT * FROM public.bill
            WHERE id = :id;
            """;

    public static final String FIND_BY_ORDER_ID_QUERY = """
            SELECT * FROM public.bill
            WHERE order_id = :orderId;
            """;

    public static final String SAVE_QUERY = """
            INSERT INTO public.bill (waiter_id, order_id, guest_id, created_at, closed_at, active, price, price_with_discount)
            VALUES (:waiterId, :orderId, :guestId, :createdAt, :closedAt, :active, :price, :priceWithDiscount);
            """;

    public static final String UPDATE_QUERY = """
            UPDATE public.bill
            SET waiter_id = :waiterId,
                order_id = :orderId,
                guest_id = :guestId,
                created_at = :createdAt,
                closed_at = :closedAt,
                active = :active,
                price = :price,
                price_with_discount = :priceWithDiscount
            WHERE id = :id;
            """;

    public static final String DELETE_QUERY = """
            DELETE FROM public.bill
            WHERE id = :id;
            """;

    public static final String FIND_ALL = """
            SELECT * FROM public.bill;
            """;

    @Override
    public Optional<Bill> findById(UUID id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(
                FIND_BY_ID_QUERY,
                parameterSource,
                billRowMapper

        ).stream().findFirst();
    }

    @Override
    public Optional<Bill> findByOrderId(UUID orderId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("orderId", orderId);

        return jdbcTemplate.query(
                FIND_BY_ORDER_ID_QUERY,
                parameterSource,
                billRowMapper
        ).stream().findFirst();
    }

    @Override
    public Bill save(Bill bill) {
        if (nonNull(bill.getId())) {
            return update(bill);
        }
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(bill);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                SAVE_QUERY,
                parameterSource,
                keyHolder
        );
        UUID generatedId = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        bill.setId(generatedId);

        return bill;
    }

    @Override
    public void delete(Bill bill) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", bill.getId());
        jdbcTemplate.update(
                DELETE_QUERY,
                parameterSource
        );
    }

    @Override
    public List<Bill> findAll() {
        return jdbcTemplate.query(
                FIND_ALL,
                billRowMapper
        );
    }

    private Bill update(Bill bill) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(bill);
        jdbcTemplate.update(
                UPDATE_QUERY,
                parameterSource
        );
        return bill;
    }
}
