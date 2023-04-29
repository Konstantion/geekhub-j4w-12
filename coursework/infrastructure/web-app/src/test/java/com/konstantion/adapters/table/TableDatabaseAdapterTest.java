package com.konstantion.adapters.table;

import com.konstantion.ApplicationStarter;
import com.konstantion.adapters.hall.HallDatabaseAdapter;
import com.konstantion.adapters.user.UserDatabaseAdapter;
import com.konstantion.configuration.RowMappersConfiguration;
import com.konstantion.hall.Hall;
import com.konstantion.table.Table;
import com.konstantion.table.TableType;
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
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfiguration.class, RowMappersConfiguration.class, ApplicationStarter.class})
@Testcontainers
@ActiveProfiles("test")
class TableDatabaseAdapterTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer = DatabaseContainer.getInstance();
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    RowMappersConfiguration rowMappers;

    TableDatabaseAdapter tableAdapter;
    HallDatabaseAdapter hallAdapter;
    UserDatabaseAdapter userAdapter;

    UUID[] HALL_IDS;
    UUID[] USER_IDS;

    @BeforeEach
    void setUp() {
        tableAdapter = new TableDatabaseAdapter(jdbcTemplate, rowMappers.tableRowMapper());
        tableAdapter.deleteAll();
        //Initialize related entities for tests
        hallAdapter = new HallDatabaseAdapter(jdbcTemplate, rowMappers.hallRowMapper());
        userAdapter = new UserDatabaseAdapter(jdbcTemplate, rowMappers.userRowMapper());
        hallAdapter.deleteAll();
        userAdapter.deleteAll();
        Hall hall = Hall.builder()
                .name("test")
                .active(true)
                .build();
        hallAdapter.save(hall);

        User waiter = User.builder()
                .active(true)
                .email("email")
                .password("password")
                .firstName("name")
                .lastName("eman")
                .roles(Role.getWaiterRole())
                .permissions(Permission.getDefaultWaiterPermission())
                .build();
        userAdapter.save(waiter);

        HALL_IDS = new UUID[]{hall.getId()};
        USER_IDS = new UUID[]{waiter.getId()};
    }

    @Test
    void shouldReturnTableWithIdWhenSaveTableWithoutId() {
        Table table = Table.builder()
                .name("table")
                .tableType(TableType.COMMON)
                .password("test")
                .hallId(HALL_IDS[0])
                .active(true)
                .build();
        tableAdapter.save(table);

        assertThat(table.getId())
                .isNotNull();
    }

    @Test
    void shouldReturnTableWhenFindById() {
        Set<UUID> waitersId = Set.of(USER_IDS[0]);
        Table table = Table.builder()
                .name("table")
                .tableType(TableType.COMMON)
                .password("test")
                .hallId(HALL_IDS[0])
                .waitersId(waitersId)
                .active(true)
                .build();
        tableAdapter.save(table);

        Optional<Table> dbTable = tableAdapter.findById(table.getId());

        assertThat(dbTable).isPresent()
                .get()
                .matches(matched -> matched.getWaitersId().equals(waitersId));
    }

    @Test
    void shouldReturnUpdatedTableWhenSaveTableWithId() {
        Table table = Table.builder()
                .name("table")
                .tableType(TableType.COMMON)
                .password("test")
                .hallId(HALL_IDS[0])
                .active(true)
                .build();
        tableAdapter.save(table);

        UUID tableId = table.getId();
        table.setName("newName");
        tableAdapter.save(table);
        Optional<Table> dbTable = tableAdapter.findById(tableId);

        assertThat(dbTable).isPresent()
                .get()
                .extracting(Table::getName).isEqualTo("newName");
    }

    @Test
    void shouldDeleteTableWhenDelete() {
        Table first = Table.builder()
                .name("first")
                .tableType(TableType.COMMON)
                .password("first")
                .hallId(HALL_IDS[0])
                .active(true)
                .build();
        Table second = Table.builder()
                .name("second")
                .tableType(TableType.COMMON)
                .password("second")
                .hallId(HALL_IDS[0])
                .active(true)
                .build();
        tableAdapter.save(first);
        tableAdapter.save(second);

        tableAdapter.delete(first);

        List<Table> dbTabled = tableAdapter.findAll();

        assertThat(dbTabled)
                .containsExactlyInAnyOrder(second);
    }

    @Test
    void shouldReturnAllTablesWhenGetAll() {
        Table first = Table.builder()
                .name("first")
                .tableType(TableType.COMMON)
                .password("first")
                .hallId(HALL_IDS[0])
                .active(true)
                .build();
        Table second = Table.builder()
                .name("second")
                .tableType(TableType.COMMON)
                .password("second")
                .hallId(HALL_IDS[0])
                .active(true)
                .build();
        tableAdapter.save(first);
        tableAdapter.save(second);

        List<Table> dbTables = tableAdapter.findAll();

        assertThat(dbTables)
                .containsExactlyInAnyOrder(first, second);
    }

    @Test
    void shouldReturnTableWhenFindByName() {
        Table table = Table.builder()
                .name("table")
                .tableType(TableType.COMMON)
                .password("test")
                .hallId(HALL_IDS[0])
                .active(true)
                .build();
        tableAdapter.save(table);

        Optional<Table> dbTable = tableAdapter.findByName("table");

        assertThat(dbTable).isPresent()
                .get()
                .isEqualTo(table);
    }

    @Test
    void shouldReturnTablesWithHallIdWhenFindWhereHallId() {
        Table first = Table.builder()
                .name("first")
                .tableType(TableType.COMMON)
                .password("first")
                .hallId(HALL_IDS[0])
                .active(true)
                .build();
        Table second = Table.builder()
                .name("second")
                .tableType(TableType.COMMON)
                .password("second")
                .hallId(HALL_IDS[0])
                .active(true)
                .build();
        Table third = Table.builder()
                .name("third")
                .tableType(TableType.COMMON)
                .password("third")
                .active(true)
                .build();
        tableAdapter.save(first);
        tableAdapter.save(second);
        tableAdapter.save(third);

        List<Table> dbTables = tableAdapter.findAllWhereHallId(HALL_IDS[0]);

        assertThat(dbTables).hasSize(2)
                .containsExactlyInAnyOrder(first, second);
    }
}
