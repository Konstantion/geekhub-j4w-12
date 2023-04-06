package com.konstantion.adapters.category;

import com.konstantion.category.Category;
import com.konstantion.category.CategoryPort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public record CategoryDatabaseAdapter(
        NamedParameterJdbcTemplate jdbcTemplate,
        RowMapper<Category> categoryRowMapper
) implements CategoryPort {

    public static final String FIND_BY_ID_QUERY = """
            SELECT * FROM public.category
            WHERE id = :id;
            """;

    @Override
    public Optional<Category> findById(UUID id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(
                FIND_BY_ID_QUERY,
                sqlParameterSource,
                categoryRowMapper
        ).stream().findFirst();
    }
}
