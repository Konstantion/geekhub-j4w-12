package com.konstantion.adapters.user;

import com.konstantion.ApplicationStarter;
import com.konstantion.configuration.RowMappersConfiguration;
import com.konstantion.testcontainers.configuration.DatabaseContainer;
import com.konstantion.testcontainers.configuration.DatabaseTestConfiguration;
import com.konstantion.user.Permission;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfiguration.class, RowMappersConfiguration.class, ApplicationStarter.class})
@Testcontainers
@ActiveProfiles("test")
class UserDatabaseAdapterTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer = DatabaseContainer.getInstance();
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    RowMappersConfiguration rowMappers;

    UserDatabaseAdapter userAdapter;

    @BeforeEach
    void setUp() {
        userAdapter = new UserDatabaseAdapter(jdbcTemplate, rowMappers.userRowMapper());
        userAdapter.deleteAll();
    }

    @Test
    void shouldReturnUserWithIdWhenSaveUserWithoutId() {
        User user = User.builder()
                .id(null)
                .active(true)
                .email("email")
                .permissions(Permission.getDefaultWaiterPermission())
                .roles(Role.getWaiterRoles())
                .password("password")
                .build();
        userAdapter.save(user);

        assertThat(user.getId())
                .isNotNull();
    }

    @Test
    void shouldReturnUserWhenFindById() {
        User user = User.builder()
                .email("email")
                .password("password")
                .roles(Role.getWaiterRoles())
                .permissions(Permission.getDefaultWaiterPermission())
                .active(true)
                .build();
        userAdapter.save(user);

        Optional<User> dbUser = userAdapter.findById(user.getId());

        assertThat(dbUser).isPresent()
                .get().isEqualTo(user);
    }

    @Test
    void shouldReturnUpdatedUserWhenSaveUserWithId() {
        User user = User.builder()
                .email("email")
                .password("password")
                .roles(Role.getWaiterRoles())
                .permissions(Permission.getDefaultWaiterPermission())
                .active(true)
                .build();
        userAdapter.save(user);

        UUID userId = user.getId();
        user.setEmail("newEmail");
        userAdapter.save(user);
        Optional<User> dbUser = userAdapter.findById(userId);

        assertThat(dbUser).isPresent()
                .get()
                .extracting(User::getEmail).isEqualTo("newEmail");
    }

    @Test
    void shouldDeleteUserWhenDelete() {
        User first = User.builder()
                .email("first")
                .password("first")
                .roles(Role.getWaiterRoles())
                .permissions(Permission.getDefaultWaiterPermission())
                .active(true)
                .build();
        User second = User.builder()
                .email("second")
                .password("second")
                .roles(Role.getAdminRoles())
                .permissions(Permission.getDefaultAdminPermission())
                .active(true)
                .build();
        userAdapter.save(first);
        userAdapter.save(second);

        userAdapter.delete(first);

        List<User> dbUsers = userAdapter.findAll();

        assertThat(dbUsers)
                .containsExactlyInAnyOrder(second);
    }

    @Test
    void shouldReturnAllUsersWhenGetAll() {
        User first = User.builder()
                .email("first")
                .password("first")
                .roles(Role.getWaiterRoles())
                .permissions(Permission.getDefaultWaiterPermission())
                .active(true)
                .build();
        User second = User.builder()
                .email("second")
                .password("second")
                .roles(Role.getAdminRoles())
                .permissions(Permission.getDefaultAdminPermission())
                .active(true)
                .build();
        userAdapter.save(first);
        userAdapter.save(second);

        List<User> dbUsers = userAdapter.findAll();

        assertThat(dbUsers)
                .containsExactlyInAnyOrder(first, second);
    }

    @Test
    void shouldReturnUserWhenFindByEmail() {
        User user = User.builder()
                .email("email")
                .password("password")
                .roles(Role.getAdminRoles())
                .permissions(Permission.getDefaultAdminPermission())
                .active(true)
                .build();
        userAdapter.save(user);

        Optional<User> dbUser = userAdapter.findByEmail("email");

        assertThat(dbUser).isPresent()
                .get()
                .isEqualTo(user);
    }
}
