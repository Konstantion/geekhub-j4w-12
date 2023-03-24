package com.konstantion.reporitories;

import com.konstantion.ragistration.token.ConfirmationToken;
import com.konstantion.ragistration.token.ConfirmationTokenRepository;
import com.konstantion.reporitories.mappers.ConfirmationTokenRowMapper;
import com.konstantion.utils.ParameterSourceUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.nonNull;

@Component
public record JdbcConfirmationTokenRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        ParameterSourceUtil parameterUtil,
        ConfirmationTokenRowMapper tokenRowMapper
) implements ConfirmationTokenRepository {
    private static final String FIND_BY_TOKEN_QUERY = """
            SELECT * FROM confirmation_token
            WHERE token = :token;
            """;

    private static final String FIND_BY_USER_ID_QUERY = """
            SELECT * FROM confirmation_token
            WHERE user_uuid = :userUuid;
            """;

    private static final String SAVE_TOKEN_QUERY = """
            INSERT INTO confirmation_token (token, created_at, expires_at, confirmed_at, user_uuid)
            VALUES (:token, :createdAt, :expiresAt, :confirmedAt, :userUuid);
            """;

    private static final String UPDATE_TOKEN_QUERY = """
            UPDATE confirmation_token
            SET token = :token,
                created_at = :createdAt,
                expires_at = :expiresAt,
                confirmed_at = :confirmedAt,
                user_uuid = :userUuid
            WHERE uuid = :uuid;
            """;

    private static final String UPDATE_CONFIRMED_AT_QUERY = """
            UPDATE confirmation_token
            SET confirmed_at = :confirmedAt
            WHERE token = :token;
            """;


    @Override
    public ConfirmationToken save(ConfirmationToken confirmationToken) {
        if (nonNull(confirmationToken.id())) {
            return update(confirmationToken);
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = parameterUtil.toParameterSource(confirmationToken);

        jdbcTemplate.update(SAVE_TOKEN_QUERY, parameters, keyHolder);

        return confirmationToken.setId((UUID) Objects.requireNonNull(keyHolder.getKeys()).get("uuid"));
    }

    private ConfirmationToken update(ConfirmationToken confirmationToken) {
        MapSqlParameterSource parameters = parameterUtil.toParameterSource(confirmationToken);
        jdbcTemplate.update(UPDATE_TOKEN_QUERY, parameters);
        return confirmationToken;
    }

    @Override
    public Optional<ConfirmationToken> findByToken(String token) {
        return jdbcTemplate.query(
                FIND_BY_TOKEN_QUERY,
                Map.of("token", token),
                tokenRowMapper
        ).stream().findAny();
    }

    @Override
    public Optional<List<ConfirmationToken>> findByUserId(UUID id) {
        return Optional.of(jdbcTemplate.query(
                FIND_BY_USER_ID_QUERY,
                Map.of("userUuid", id),
                tokenRowMapper
        ));
    }

    @Override
    public int updateConfirmedAt(String token, LocalDateTime confirmedAt) {
        return jdbcTemplate.update(
                UPDATE_CONFIRMED_AT_QUERY,
                Map.of(
                        "token", token,
                        "confirmedAt", confirmedAt
                )
        );
    }
}
