package com.konstantion.adapters.guest;

import com.konstantion.guest.Guest;
import com.konstantion.guest.GuestPort;
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
public class GuestDatabaseAdapter implements GuestPort {
    private NamedParameterJdbcTemplate jdbcTemplate;
    private RowMapper<Guest> guestRowMapper;
    public static final String FIND_BY_ID_QUERY = """
            SELECT * FROM public.guest
            WHERE id = :id;
            """;

    public static final String FIND_BY_ID_NAME = """
            SELECT * FROM public.guest
            WHERE name = :name;
            """;

    public static final String DELETE_QUERY = """
            DELETE FROM public.guest
            WHERE id = :id;
            """;

    public static final String SAVE_QUERY = """
            INSERT INTO public.guest (name, phone_number, discount_percent, created_at, total_spent_sum, active)
            VALUES (:name, :phoneNumber, :discountPercent, :createdAt, :totalSpentSum, :active);
            """;

    public static final String UPDATE_QUERY = """
            UPDATE public.guest
                SET name = :name,
                phone_number = :phoneNumber,
                discount_percent = :discountPercent,
                created_at = :createdAt,
                total_spent_sum = :totalSpentSum,
                active = :active
            WHERE id = :id;
            """;

    public static final String FIND_ALL_QUERY = """
            SELECT * FROM public.guest;
            """;

    @Override
    public List<Guest> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_QUERY,
                guestRowMapper
        );
    }

    @Override
    public Optional<Guest> findById(UUID id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.query(
                FIND_BY_ID_QUERY,
                parameterSource,
                guestRowMapper
        ).stream().findFirst();
    }

    @Override
    public Guest save(Guest guest) {
        if (nonNull(guest.getId())) {
            return update(guest);
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(guest);
        jdbcTemplate.update(
                SAVE_QUERY,
                parameterSource,
                keyHolder
        );
        UUID generatedId = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        guest.setId(generatedId);

        return guest;
    }

    @Override
    public void delete(Guest guest) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", guest.getId());
        jdbcTemplate.update(
                DELETE_QUERY,
                parameterSource
        );
    }

    @Override
    public Optional<Guest> findByName(String name) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", name);
        return jdbcTemplate.query(
                FIND_BY_ID_NAME,
                parameterSource,
                guestRowMapper
        ).stream().findFirst();
    }

    private Guest update(Guest guest) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(guest);
        jdbcTemplate.update(
                UPDATE_QUERY,
                parameterSource
        );
        return guest;
    }
}
