package com.konstantion.adapters.hall;

import com.konstantion.hall.Hall;
import com.konstantion.hall.HallPort;
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
public class HallDatabaseAdapter implements HallPort {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Hall> hallRowMapper;

    public HallDatabaseAdapter(NamedParameterJdbcTemplate jdbcTemplate, RowMapper<Hall> hallRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.hallRowMapper = hallRowMapper;
    }

    public static final String FIND_BY_ID_QUERY = """
            SELECT * FROM public.hall
            WHERE id = :id;
            """;

    public static final String SAVE_QUERY = """
            INSERT INTO public.hall (name, created_at, active)
            VALUES (:name, :createdAt, :active);
            """;

    public static final String UPDATE_QUERY = """
            UPDATE public.hall
               SET name = :name,
                   created_at = :createdAt,
                   active = :active
               WHERE id = :id;
            """;
    public static final String DELETE_QUERY = """
            DELETE FROM public.hall
            WHERE id = :id;
            """;
    public static final String FIND_ALL_QUERY = """
            SELECT * FROM public.hall;
            """;

    @Override
    public List<Hall> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_QUERY,
                hallRowMapper
        );
    }

    @Override
    public Optional<Hall> findById(UUID id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.query(
                FIND_BY_ID_QUERY,
                parameterSource,
                hallRowMapper
        ).stream().findFirst();
    }

    @Override
    public Hall save(Hall hall) {
        if (nonNull(hall.getId())) {
            return update(hall);
        }

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(hall);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                SAVE_QUERY,
                parameterSource,
                keyHolder
        );

        UUID generatedId = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        hall.setId(generatedId);

        return hall;
    }

    @Override
    public void delete(Hall hall) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", hall.getId());
        jdbcTemplate.update(
                DELETE_QUERY,
                parameterSource
        );
    }

    private Hall update(Hall hall) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(hall);
        jdbcTemplate.update(
                UPDATE_QUERY,
                parameterSource
        );
        return hall;
    }
}
