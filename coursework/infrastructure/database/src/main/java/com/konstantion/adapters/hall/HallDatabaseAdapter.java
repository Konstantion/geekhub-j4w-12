package com.konstantion.adapters.hall;

import com.konstantion.hall.Hall;
import com.konstantion.hall.HallPort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public record HallDatabaseAdapter(
        NamedParameterJdbcTemplate jdbcTemplate,
        RowMapper<Hall> hallRowMapper
) implements HallPort {
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
        return null;
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
