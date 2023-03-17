package com.konstantion.reporitories;

import com.konstantion.category.Category;
import com.konstantion.category.CategoryRepository;
import com.konstantion.utils.ParameterSourceUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public record JdbcCategoryRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        ParameterSourceUtil parameterUtil
) implements CategoryRepository {
    private static final String FIND_BY_ID_QUERY = """
            SELECT * FROM category
            WHERE uuid = :uuid;
            """;

    private static final String FIND_ALL_QUERY = """
            SELECT * FROM category;
            """;

    private static final String INSERT_INTO_CATEGORY_QUERY = """
            INSERT INTO  category (name, created_at, user_uuid)
            VALUES (:name, :createdAt, :userUuid);
            """;
    private static final String UPDATE_CATEGORY_QUERY = """
            UPDATE category
            SET name = :name,
                created_at = :createdAt,
                user_uuid = :userUuid
            WHERE uuid = :uuid;
            """;

    private static final String DELETE_CATEGORY_STRING = """
            DELETE FROM category WHERE uuid = :uuid;
            """;

    @Override
    public List<Category> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_QUERY,
                new BeanPropertyRowMapper<>()
        );
    }

    @Override
    public Optional<Category> findById(UUID uuid) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY,
                        Map.of("uuid", uuid),
                        new BeanPropertyRowMapper<Category>()
                ).stream().findFirst();
    }

    @Override
    public Category save(Category category) {
        if (category.uuid() != null) {
            return update(category);
        }
        MapSqlParameterSource parameters = parameterUtil.toParameterSource(category);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(INSERT_INTO_CATEGORY_QUERY, parameters, keyHolder);
        return category.setUuid((UUID) Objects.requireNonNull(keyHolder.getKeys()).get("uuid"));
    }

    @Override
    public void delete(Category category) {
        deleteById(category.uuid());
    }

    @Override
    public void deleteById(UUID uuid) {
        jdbcTemplate.update(DELETE_CATEGORY_STRING, Map.of("uuid", uuid));
    }

    private Category update(Category category) {
        MapSqlParameterSource parameters = parameterUtil.toParameterSource(category);
        jdbcTemplate.update(UPDATE_CATEGORY_QUERY, parameters);
        return category;
    }

}
