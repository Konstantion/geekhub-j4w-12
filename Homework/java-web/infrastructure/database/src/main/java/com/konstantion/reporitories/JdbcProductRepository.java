package com.konstantion.reporitories;

import com.konstantion.product.Product;
import com.konstantion.product.ProductRepository;
import com.konstantion.reporitories.mappers.ProductRowMapper;
import com.konstantion.utils.ParameterSourceUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public record JdbcProductRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                    ProductRowMapper productRawMapper,
                                    ParameterSourceUtil parameterUtil)
        implements ProductRepository {

    private static final String FIND_ALL_QUERY = """
                   SELECT * FROM product;
            """;
    private static final String INSERT_PRODUCT_QUERY = """
                    INSERT INTO product (name, price, created_at, user_uuid, image_bytes, description, category_uuid)
                    VALUES (:name, :price, :createdAt, :userUuid, :imageBytes, :description, :categoryUuid);
            """;

    private static final String DELETE_BY_UUID_PRODUCT_QUERY = """
                    DELETE FROM product WHERE uuid = :uuid;
            """;
    private static final String UPDATE_PRODUCT_QUERY = """
                    UPDATE product
                    SET name = :name,
                        price = :price,
                        created_at = :createdAt,
                        user_uuid = :userUuid,
                        image_bytes = :imageBytes,
                        description = :description,
                        category_uuid = :categoryUuid
                    WHERE uuid = :uuid;
            """;

    private static final String FIND_BY_ID_QUERY = """
                    SELECT * FROM product WHERE uuid = :uuid;
            """;

    private static final BiFunction<String, UUID, String> TOTAL_COUNT_QUERY =
            (orderParameter, categoryUuid) -> {
                String findByCategory = categoryUuid == null ? "" : "AND category_uuid = :categoryUuid ";
                return "SELECT COUNT(*) FROM product " +
                       "WHERE LOWER(product.name) LIKE LOWER(:searchParameter) " +
                       findByCategory + ";";
            };


    private static final BiFunction<String, UUID, String> FIND_ALL_PAGE_QUERY =
            (orderParameter, categoryUuid) -> {
                String findByCategory = categoryUuid == null ? "" : "AND category_uuid = :categoryUuid ";
                return "SELECT product.*, COALESCE(avg(r.rating), 0) AS rating FROM product " +
                       "LEFT JOIN review r ON product.uuid = r.product_uuid " +
                       "WHERE LOWER(product.name) LIKE LOWER(:searchParameter) " +
                       findByCategory +
                       "GROUP BY product.uuid, name, price, product.created_at, product.user_uuid, image_bytes, description, category_uuid " +
                       "ORDER BY " + orderParameter + " LIMIT :limit OFFSET :offset;";
            };

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,
                        Map.of("uuid", uuid),
                        productRawMapper)
        );
    }

    @Override
    public List<Product> findAll() {
        return findAll(Sort.by(ASC, "id"));
    }

    @Override
    public List<Product> findAll(Sort sort) {
        return jdbcTemplate.query(FIND_ALL_QUERY, productRawMapper).stream().sorted(getComparator(sort)).toList();
    }

    @Override
    public Product save(Product product) {
        if (nonNull(product.uuid())) {
            return update(product);
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = parameterUtil.toParameterSource(product);

        jdbcTemplate.update(INSERT_PRODUCT_QUERY, parameters, keyHolder);

        return product.setUuid((UUID) Objects.requireNonNull(keyHolder.getKeys()).get("uuid"));
    }

    private Product update(Product product) {
        MapSqlParameterSource parameters = parameterUtil.toParameterSource(product);
        jdbcTemplate.update(UPDATE_PRODUCT_QUERY, parameters);
        return product;
    }

    @Override
    public void delete(Product product) {
        deleteById(product.uuid());
    }

    @Override
    public void deleteById(UUID uuid) {
        jdbcTemplate.update(DELETE_BY_UUID_PRODUCT_QUERY, Map.of("uuid", uuid));
    }

    @Override
    public Page<Product> findAll(
            Integer pageNumber, Integer pageSize,
            String field, String pattern,
            UUID categoryUuid) {
        int offset = (pageNumber - 1) * pageSize;
        pattern = "%" + pattern + "%";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("limit", pageSize)
                .addValue("offset", offset)
                .addValue("searchParameter", pattern)
                .addValue("categoryUuid", categoryUuid);


        List<Product> products = jdbcTemplate.query(
                FIND_ALL_PAGE_QUERY.apply(field, categoryUuid),
                params,
                productRawMapper);

        Integer totalCount = requireNonNullElse(jdbcTemplate.queryForObject(
                TOTAL_COUNT_QUERY.apply(field, categoryUuid),
                params,
                Integer.class
        ), products.size());

        return new PageImpl<>(products, PageRequest.of(pageNumber - 1, pageSize), totalCount);
    }

    public Comparator<Product> getComparator(Sort sort) {
        Comparator<Product> comparator;
        Sort.Order order = sort.iterator().next();
        comparator = switch (order.getProperty()) {
            case "name" -> Comparator.comparing(Product::name);
            case "price" -> Comparator.comparing(Product::price);
            default -> Comparator.comparing(Product::name);
        };

        if (order.getDirection().equals(Sort.Direction.DESC)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
