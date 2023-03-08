package com.konstantion.reporitories;

import com.konstantion.product.Product;
import com.konstantion.product.ProductRepository;
import com.konstantion.reporitories.mappers.ProductRawMapper;
import com.konstantion.reporitories.mappers.ProductReviewRawMapper;
import com.konstantion.review.Review;
import com.konstantion.utils.ParameterSourceUtil;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public record JdbcProductRepository(NamedParameterJdbcTemplate jdbcTemplate, ProductRawMapper productRawMapper,
                                    ProductReviewRawMapper productReviewRawMapper,
                                    ParameterSourceUtil parameterUtil) implements ProductRepository {

    private static final String FIND_ALL_QUERY = """
                   SELECT * FROM product;
            """;
    private static final String INSERT_PRODUCT_QUERY = """
                    INSERT INTO product (name, price, created_at, user_uuid, image_path)
                    VALUES (:name, :price, :createdAt, :userUuid, :imagePath);
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
                        image_path = :imagePath
                    WHERE uuid = :uuid;
            """;

    private static final String FIND_BY_UUID_QUERY = """
                    SELECT * FROM product WHERE uuid = :uuid;
            """;

    private static final String FIND_ALL_WITH_REVIEW = """
                SELECT product.*, review.*
                FROM product
                LEFT JOIN review ON product.uuid = review.product_uuid;
            """;


    @Override
    public Optional<Product> findByUuid(UUID uuid) {
        return Optional.ofNullable(jdbcTemplate.query(FIND_BY_UUID_QUERY, Map.of("uuid", uuid), productRawMapper).get(0));
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
    public Map<Product, List<Review>> findAllProductsWithReviews() {
        List<Map.Entry<Product, Review>> entries =
                jdbcTemplate.query(FIND_ALL_WITH_REVIEW, productReviewRawMapper);
        return entries.stream().collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
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
        deleteByUuid(product.uuid());
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        jdbcTemplate.update(DELETE_BY_UUID_PRODUCT_QUERY, Map.of("uuid", uuid));
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
