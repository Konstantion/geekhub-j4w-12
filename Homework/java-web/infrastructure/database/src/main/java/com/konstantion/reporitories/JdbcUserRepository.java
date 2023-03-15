package com.konstantion.reporitories;

import com.konstantion.user.User;
import com.konstantion.reporitories.mappers.UserRawMapper;
import com.konstantion.user.UserRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public record JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate, UserRawMapper rawMapper) implements UserRepository {

    private static final String FIND_BY_ID_QUERY = """
                    SELECT * FROM users WHERE uuid = :uuid;
            """;

    @Override
    public Optional<User> findUserById(UUID uuid) {
        return Optional.of(jdbcTemplate.query(FIND_BY_ID_QUERY, Map.of("uuid", uuid), rawMapper).get(0));
    }
}
