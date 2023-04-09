package com.konstantion.adapters.user;

import com.konstantion.user.Permission;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import com.konstantion.user.UserPort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
public record UserDatabaseAdapter(
        NamedParameterJdbcTemplate jdbcTemplate,
        RowMapper<User> userRowMapper
) implements UserPort {
    private static final String FIND_BY_ID = """
            SELECT * FROM public.user
            WHERE id = :id;
            """;

    private static final String DELETE_QUERY = """
            DELETE FROM public.user
            WHERE id = :id;
            """;

    private static final String SAVE_QUERY = """
            INSERT INTO public."user" (first_name, last_name, email, phone_number, age, password, created_at, active)
            VALUES (:firstName, :lastName, :email, :phoneNumber, :age, :password, :createdAt, :active);
            """;

    private static final String UPDATE_QUERY = """
            UPDATE public."user"
            SET first_name   = :firstName,
                last_name    = :lastName,
                email        = :email,
                phone_number = :phoneNumber,
                age          = :age,
                password     = :password,
                active       = :active
            WHERE id = :id;
            """;
    private static final String FIND_ROLES_BY_USER_ID = """
            SELECT role FROM public.user_role
            WHERE user_id = :userId;
            """;

    private static final String FIND_PERMISSION_BY_USER_ID = """
            SELECT permission FROM public.user_permission
            WHERE user_id = :userId;
            """;

    private static final String DELETE_ROLES = """
            DELETE FROM public.user_role
            WHERE user_id = :userId;
            """;

    private static final String DELETE_PERMISSIONS = """
            DELETE FROM public.user_permission
            WHERE user_id = :userId;
            """;

    private static final String SAVE_ROLE = """
            INSERT INTO public.user_role (user_id, role)
            VALUES (:userId, :role);
            """;

    private static final String SAVE_PERMISSION = """
            INSERT INTO public.user_permission (user_id, permission)
            VALUES (:userId, :permission);
            """;

    @Override
    public User save(User user) {
        if (nonNull(user.getId())) {
            return update(user);
        }

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                SAVE_QUERY,
                parameterSource,
                keyHolder
        );

        UUID generatedId = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        user.setId(generatedId);

        updateUserRoles(user);
        updateUserPermissions(user);

        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        User user = jdbcTemplate.query(
                FIND_BY_ID,
                parameterSource,
                userRowMapper
        ).stream().findFirst().orElse(null);

        if (nonNull(user)) {
            user.setRoles(findRolesByUserId(user.getId()));
            user.setPermissions(findPermissionsByUserId(user.getId()));
        }

        return Optional.ofNullable(user);
    }

    @Override
    public void delete(User user) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", user.getId());
        jdbcTemplate.update(
                DELETE_QUERY,
                parameterSource
        );
    }

    private User update(User user) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        jdbcTemplate.update(
                UPDATE_QUERY,
                parameterSource
        );

        updateUserRoles(user);
        updateUserPermissions(user);

        return user;
    }

    private void updateUserRoles(User user) {
        deleteUserRoles(user.getId());
        saveUserRoles(user.getId(), user.getRoles());
    }

    private void updateUserPermissions(User user) {
        deleteUserPermissions(user.getId());
        saveUserPermissions(user.getId(), user.getPermissions());
    }

    private void deleteUserRoles(UUID userId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", userId);
        jdbcTemplate.update(
                DELETE_ROLES,
                parameterSource
        );
    }

    private void deleteUserPermissions(UUID userId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", userId);
        jdbcTemplate.update(
                DELETE_PERMISSIONS,
                parameterSource
        );
    }

    private void saveUserRoles(UUID userId, Set<Role> roles) {
        roles.forEach(role -> {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("role", role.name());
            jdbcTemplate.update(
                    SAVE_ROLE,
                    parameterSource
            );
        });
    }

    private void saveUserPermissions(UUID userId, Set<Permission> permissions) {
        permissions.forEach(permission -> {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("permission", permission.name());
            jdbcTemplate.update(
                    SAVE_PERMISSION,
                    parameterSource
            );
        });
    }

    private Set<Role> findRolesByUserId(UUID userId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", userId);

        List<String> rolesString = jdbcTemplate.queryForList(
                FIND_ROLES_BY_USER_ID,
                parameterSource,
                String.class
        );

        return rolesString.stream()
                .filter(role -> Arrays.asList(Role.values()).contains(role))
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    private Set<Permission> findPermissionsByUserId(UUID userId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", userId);

        List<String> permissionsString = jdbcTemplate.queryForList(
                FIND_PERMISSION_BY_USER_ID,
                parameterSource,
                String.class
        );

        return permissionsString.stream()
                .filter(permission -> Arrays.asList(Permission.values()).contains(permission))
                .map(Permission::valueOf)
                .collect(Collectors.toSet());
    }

}
