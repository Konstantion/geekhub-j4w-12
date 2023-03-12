package com.konstantion.reporitories;

import com.konstantion.review.Review;
import com.konstantion.reporitories.mappers.ReviewRawMapper;
import com.konstantion.review.ReviewRepository;
import com.konstantion.utils.ParameterSourceUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
public record JdbcReviewRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                   ReviewRawMapper reviewRawMapper,
                                   ParameterSourceUtil parameterUtil
) implements ReviewRepository {
    private static final String DELETE_BY_UUID_REVIEW_QUERY = """
                    DELETE FROM review WHERE uuid = :uuid;
            """;

    private static final String FIND_BY_UUID_QUERY = """
                    SELECT * FROM review WHERE uuid = :uuid;
            """;

    private static final String INSERT_REVIEW_QUERY = """
            INSERT INTO review (uuid, message, rating, user_uuid, product_uuid, created_at)
            VALUES (:uuid, :message, :rating, :userUuid, :productUuid, :createdAt);
            """;

    private static final String FIND_BY_USER_ID_QUERY = """
                SELECT * FROM review
                WHERE user_uuid = :userUuid;
            """;

    private static final String FIND_BY_PRODUCT_ID_QUERY = """
                 SELECT * FROM review
                 WHERE product_uuid = :productUuid;
            """;

    @Override
    public Optional<Review> findById(UUID uuid) {
        return Optional.ofNullable(jdbcTemplate.query(FIND_BY_UUID_QUERY, Map.of("uuid", uuid), reviewRawMapper).get(0));
    }

    @Override
    public List<Review> findByUserId(UUID uuid) {
        return jdbcTemplate.query(
                FIND_BY_USER_ID_QUERY,
                Map.of("userUuid", uuid),
                reviewRawMapper
        );
    }

    @Override
    public List<Review> findByProductId(UUID uuid) {
        return jdbcTemplate.query(
                FIND_BY_PRODUCT_ID_QUERY,
                Map.of("productUuid", uuid),
                reviewRawMapper
        );
    }

    @Override
    public Review save(Review review) {
        MapSqlParameterSource parameterSource = parameterUtil.toParameterSource(review);
        jdbcTemplate.update(INSERT_REVIEW_QUERY, parameterSource);

        return review;
    }

    @Override
    public void delete(Review review) {
        deleteById(review.uuid());
    }

    @Override
    public void deleteById(UUID uuid) {
        jdbcTemplate.update(DELETE_BY_UUID_REVIEW_QUERY, Map.of("uuid", uuid));
    }
}
