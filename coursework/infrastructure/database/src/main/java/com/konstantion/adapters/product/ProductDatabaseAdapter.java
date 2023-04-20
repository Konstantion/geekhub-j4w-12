package com.konstantion.adapters.product;

import com.konstantion.product.Product;
import com.konstantion.product.ProductPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;

@Repository
public class ProductDatabaseAdapter implements ProductPort {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Product> productRowMapper;

    public ProductDatabaseAdapter(NamedParameterJdbcTemplate jdbcTemplate, RowMapper<Product> productRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRowMapper = productRowMapper;
    }

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

    private static final BiFunction<String, UUID, String> FIND_ALL_QUERY =
            (orderParameter, categoryId) -> {
                String findByCategoryId = categoryId == null ? "" : "AND category_id = :categoryId ";
                return "SELECT * FROM public.product " +
                       "WHERE LOWER(product.name) LIKE LOWER(:searchPattern) " +
                       "AND active = :active " +
                       findByCategoryId +
                       "GROUP BY product.uuid, name, price, product.created_at, product.user_uuid, image_bytes, description, category_uuid " +
                       "ORDER BY " + orderParameter + " LIMIT :limit OFFSET :offset;";
            };

    private static final Function<UUID, String> TOTAL_COUNT_QUERY =
            categoryId -> {
                String findByCategoryId = categoryId == null ? "" : "AND category_id = :categoryId ";
                return "SELECT COUNT(*) FROM public.product " +
                       "WHERE LOWER(product.name) LIKE LOWER(:searchParameter) " +
                       "AND active = :active " +
                       findByCategoryId + ";";
            };


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

    @Override
    public Page<Product> findAll(
            int pageNumber,
            int pageSize,
            String orderBy,
            String searchPattern,
            UUID categoryId,
            boolean ascending,
            boolean active
    ) {
        int offset = (pageNumber - 1) * pageSize;
        searchPattern = "%" + searchPattern + "%";
        orderBy = ascending ? orderBy + " ASC " : orderBy + " DESC ";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("limit", pageSize)
                .addValue("offset", offset)
                .addValue("searchPattern", searchPattern)
                .addValue("categoryId", categoryId)
                .addValue("active", active);

        List<Product> products = jdbcTemplate.query(
                FIND_ALL_QUERY.apply(orderBy, categoryId),
                parameterSource,
                productRowMapper);

        Integer totalCount = requireNonNullElse(jdbcTemplate.queryForObject(
                TOTAL_COUNT_QUERY.apply(categoryId),
                parameterSource,
                Integer.class
        ), products.size());

        return new PageImpl<>(products, PageRequest.of(pageNumber - 1, pageSize), totalCount);
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
