package com.konstantion.reporitories;

import com.konstantion.reporitories.mappers.RoleRowMapper;
import com.konstantion.reporitories.mappers.UserRowMapper;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import com.konstantion.user.UserRepository;
import com.konstantion.utils.ParameterSourceUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Objects.nonNull;

@Component
public record JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                 UserRowMapper userMapper,
                                 ParameterSourceUtil parameterUtil,
                                 RoleRowMapper roleRowMapper) implements UserRepository {

    private static final String FIND_BY_ID_QUERY = """
                    SELECT * FROM public.user WHERE uuid = :uuid;
            """;

    private static final String FIND_BY_EMAIL_QUERY = """
                    SELECT * FROM public.user WHERE email = :email;
            """;

    private static final String INSERT_USER_QUERY = """
                    INSERT INTO public.user (email, first_name, password, last_name, phone_number, enabled, non_locked)
                    VALUES (:email, :firstName, :password, :lastName, :phoneNumber, :enabled, :nonLocked);
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

    private static final String FIND_ROLES_BY_USER_ID_QUERY = """
            SELECT name FROM user_role
            WHERE user_uuid = :userUuid;
            """;

    private static final String SAVE_USER_ROLE_QUERY = """
            INSERT INTO user_role
            VALUES (:userUuid, :roleName)
            """;

    private static final String DELETE_USER_ROLES_QUERY = """
            DELETE FROM user_role
            WHERE user_uuid = :userUuid;
            """;

    private static final String DELETE_USER_QUERY = """
            DELETE FROM public.user
            WHERE uuid = :uuid;
            """;

    @Override
    public Optional<User> findById(UUID uuid) {
        User user = jdbcTemplate.query(
                FIND_BY_ID_QUERY,
                Map.of("uuid", uuid),
                userMapper).stream().findAny().orElse(null);
        if (nonNull(user)) {
            Set<Role> userRoles = new HashSet<>(jdbcTemplate.query(
                    FIND_ROLES_BY_USER_ID_QUERY,
                    Map.of("userUuid", uuid),
                    roleRowMapper)
            );
            user.setRoles(userRoles);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User save(User user) {
        if (user.getId() != null) {
            return update(user);
        }

        MapSqlParameterSource parameters = parameterUtil.toParameterSource(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                INSERT_USER_QUERY,
                parameters,
                keyHolder
        );
        user.setId((UUID) Objects.requireNonNull(keyHolder.getKeys()).get("uuid"));

        updateUserRoles(user);

        return user;
    }

    @Override
    public UUID setEnableById(UUID id, boolean enabled) {
        jdbcTemplate.update(
                SET_USER_ENABLED_QUERY,
                Map.of(
                        "enabled", enabled,
                        "uuid", id
                )
        );
        return id;
    }

    @Override
    public UUID setNonLockedById(UUID id, boolean nonLocked) {
        jdbcTemplate.update
                (SET_USER_NON_LOCKED_QUERY,
                        Map.of(
                                "nonLocked", nonLocked,
                                "uuid", id
                        ));
        return id;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user = jdbcTemplate.query(
                FIND_BY_EMAIL_QUERY,
                Map.of("email", email),
                userMapper).stream().findAny().orElse(null);
        if (nonNull(user)) {
            Set<Role> userRoles = new HashSet<>(jdbcTemplate.query(
                    FIND_ROLES_BY_USER_ID_QUERY,
                    Map.of("userUuid", user.getId()),
                    roleRowMapper)
            );
            user.setRoles(userRoles);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void delete(User user) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue( "uuid", user.getId());

        jdbcTemplate.update(
                DELETE_USER_QUERY,
                parameterSource
        );
    }

    private User update(User user) {
        SqlParameterSource parameters = parameterUtil()
                .toParameterSource(user);
        jdbcTemplate.update(
                UPDATE_USER_QUERY,
                parameters
        );
        updateUserRoles(user);

        return user;
    }

    private void updateUserRoles(User user) {
        if(nonNull(user.getId())) {
            deleteUserRoles(user.getId());
        }
        saveUserRoles(user);
    }

    private void deleteUserRoles(UUID userId) {
        jdbcTemplate.update(
                DELETE_USER_ROLES_QUERY,
                Map.of("userUuid", userId)
        );
    }

    private void saveUserRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (nonNull(roles)) {
            user.getRoles().forEach(
                    role -> saveUserRole(user.getId(), role)
            );
        }
    }

    private void saveUserRole(UUID userUuid, Role role) {
        jdbcTemplate.update(
                SAVE_USER_ROLE_QUERY,
                Map.of(
                        "userUuid", userUuid,
                        "roleName", role.name()
                )
        );
    }
}
