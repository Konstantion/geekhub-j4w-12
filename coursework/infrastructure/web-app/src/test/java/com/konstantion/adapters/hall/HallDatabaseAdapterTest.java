package com.konstantion.adapters.hall;

import com.konstantion.ApplicationStarter;
import com.konstantion.configuration.RowMappersConfiguration;
import com.konstantion.hall.Hall;
import com.konstantion.testcontainers.configuration.DatabaseContainer;
import com.konstantion.testcontainers.configuration.DatabaseTestConfiguration;
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
class HallDatabaseAdapterTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer = DatabaseContainer.getInstance();
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    RowMappersConfiguration rowMappers;

    HallDatabaseAdapter hallAdapter;

    @BeforeEach
    public void setUp() {
        hallAdapter = new HallDatabaseAdapter(jdbcTemplate, rowMappers.hallRowMapper());
        hallAdapter.deleteAll();
    }

    @Test
    void shouldReturnHallWithIdWhenSaveHallWithoutId() {
        Hall hall = Hall.builder()
                .active(true)
                .id(null)
                .name("test")
                .build();
        hallAdapter.save(hall);

        assertThat(hall.getId()).isNotNull();
    }

    @Test
    void shouldUpdateHallWhenSaveHallWithId() {
        Hall hall = Hall.builder().active(true)
                .active(true)
                .id(null)
                .name("test")
                .build();
        hallAdapter.save(hall);
        UUID id = hall.getId();

        hall.setName("newName");
        hallAdapter.save(hall);

        Optional<Hall> dbHall = hallAdapter.findById(id);

        assertThat(dbHall).isPresent()
                .get()
                .matches(matched -> matched.getId().equals(id)
                                    && matched.getName().equals("newName"));
    }

    @Test
    void shouldReturnHallsWhenFindAll() {
        Hall first = Hall.builder()
                .active(true)
                .id(null)
                .name("first")
                .build();
        Hall second = Hall.builder()
                .active(true)
                .id(null)
                .name("second")
                .build();
        hallAdapter.save(first);
        hallAdapter.save(second);

        List<Hall> dbHall = hallAdapter.findAll();

        assertThat(dbHall)
                .hasSize(2)
                .containsExactlyInAnyOrder(first, second);
    }

    @Test
    void shouldDeleteHallWhenDeleteHall() {
        Hall first = Hall.builder()
                .active(true)
                .id(null)
                .name("first")
                .build();
        Hall second = Hall.builder()
                .active(true)
                .id(null)
                .name("second")
                .build();
        hallAdapter.save(first);
        hallAdapter.save(second);

        hallAdapter.delete(first);
        List<Hall> dbHalls = hallAdapter.findAll();

        assertThat(dbHalls)
                .hasSize(1)
                .containsExactlyInAnyOrder(second);
    }
}
