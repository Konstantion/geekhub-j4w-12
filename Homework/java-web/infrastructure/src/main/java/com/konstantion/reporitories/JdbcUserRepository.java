package com.konstantion.reporitories;

import com.konstantion.user.User;
import com.konstantion.user.UserRawMapper;
import com.konstantion.user.UserRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public record JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate, UserRawMapper rawMapper) implements UserRepository {

    private static final String FIND_BY_ID_QUERY = """
                    SELECT * FROM users WHERE id = :id;
            """;

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.of(jdbcTemplate.query(FIND_BY_ID_QUERY, Map.of("id", id), rawMapper).get(0));
    }
}
