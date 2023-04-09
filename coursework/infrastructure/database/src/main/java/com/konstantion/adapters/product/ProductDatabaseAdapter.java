package com.konstantion.adapters.product;

import com.konstantion.product.Product;
import com.konstantion.product.ProductPort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Component
public record ProductDatabaseAdapter(
        NamedParameterJdbcTemplate jdbcTemplate,
        RowMapper<Product> productRowMapper
) implements ProductPort {
    private static final String FIND_BY_ID_QUERY = """
            SELECT * FROM public.product
            WHERE id = :id;
            """;

    private static final String SAVE_QUERY = """
            INSERT INTO public.product (name, price, weight, category_id, image_bytes, description, creator_id, created_at, deactivate_at, active)
            VALUES (:name, :price, :weight, :categoryId, :imageBytes, :description, :creatorId, :createdAt, :deactivateAt, :active)
            """;

    private static final String DELETE_QUERY = """
            DELETE FROM public.product
            WHERE id = :id;
            """;

    private static final String UPDATE_QUERY = """
            UPDATE public.product
            SET name = :name,
                price = :price,
                weight = :weight,
                category_id = :categoryId,
                image_bytes = :imageBytes,
                description = :description,
                created_at = :categoryId,
                deactivate_at = :deactivateAt,
                active = :active
            WHERE id = :id;
            """;


    @Override
    public Product save(Product product) {
        if (nonNull(product.getId())) {
            return update(product);
        }

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(product);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                SAVE_QUERY,
                parameterSource,
                keyHolder
        );

        UUID generatedId = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        product.setId(generatedId);

        return product;
    }

    @Override
    public void delete(Product product) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", product.getId());
        jdbcTemplate.update(
                DELETE_QUERY,
                parameterSource
        );
    }

    @Override
    public Optional<Product> findById(UUID id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.query(
                FIND_BY_ID_QUERY,
                parameterSource,
                productRowMapper
        ).stream().findFirst();
    }

    private Product update(Product product) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(product);
        jdbcTemplate.update(
                UPDATE_QUERY,
                sqlParameterSource
        );
        return product;
    }
}
