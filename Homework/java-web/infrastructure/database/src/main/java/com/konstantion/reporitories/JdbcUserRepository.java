package com.konstantion.reporitories;

import com.konstantion.reporitories.mappers.UserRawMapper;
import com.konstantion.user.User;
import com.konstantion.user.UserRepository;
import com.konstantion.utils.ParameterSourceUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public record JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                 UserRawMapper userMapper,
                                 ParameterSourceUtil parameterUtil) implements UserRepository {

    private static final String FIND_BY_ID_QUERY = """
                    SELECT * FROM public.user WHERE uuid = :uuid;
            """;

    private static final String FIND_BY_EMAIL_QUERY = """
                    SELECT * FROM public.user WHERE email = :email;
            """;

    private static final String INSERT_USER_QUERY = """
                    INSERT INTO public.user (email, first_name, password, last_name, phone_number, enabled, non_locked)
                    VALUES (:email, :first_name, :password, :last_name, :phone_number, :enabled, :nonLocked);
            """;

    private static final String UPDATE_USER_QUERY = """
            UPDATE public.user
            SET first_name = :firstName,
                last_name = :lastName,
                password = :password,
                email = :email,
                phone_number = :phoneNumber,
                enabled = :enabled,
                non_locked = :nonLocked
            WHERE uuid = :uuid;
            """;

    private static final String SET_USER_ENABLED_QUERY = """
            UPDATE public.user
            SET enabled = :enabled
            WHERE uuid = :uuid;
            """;

    private static final String SET_USER_NON_LOCKED_QUERY = """
            UPDATE public.user
            SET non_locked = :nonLocked
            WHERE uuid = :uuid;
            """;

    @Override
    public Optional<User> findUserById(UUID uuid) {
        return Optional.of(jdbcTemplate.query(FIND_BY_ID_QUERY, Map.of("uuid", uuid), userMapper).get(0));
    }

    @Override
    public User save(User user) {
        if (user.getId() != null) {
            return update(user);
        }

        MapSqlParameterSource parameters = parameterUtil.toParameterSource(user);
        return jdbcTemplate.query(INSERT_USER_QUERY, parameters, userMapper).get(0);
    }

    @Override
    public UUID setEnableById(UUID id, boolean enabled) {
        jdbcTemplate.update(SET_USER_ENABLED_QUERY, Map.of("enabled", enabled));
        return id;
    }

    @Override
    public UUID setNonLockedById(UUID id, boolean nonLocked) {
        jdbcTemplate.update(SET_USER_NON_LOCKED_QUERY, Map.of("nonLocked", nonLocked));
        return id;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.of(jdbcTemplate.query(FIND_BY_EMAIL_QUERY, Map.of("email", email), userMapper).get(0));
    }

    private User update(User user) {
        SqlParameterSource parameters = parameterUtil().toParameterSource(user);
        jdbcTemplate.update(UPDATE_USER_QUERY, parameters);
        return user;
    }
}
