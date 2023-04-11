package com.konstantion.adapters.table;

import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Component
public record TableDatabaseAdapter(
        NamedParameterJdbcTemplate jdbcTemplate,
        RowMapper<Table> tableRowMapper
) implements TablePort {
    private static final String FIND_ALL_QUERY = """
            SELECT * FROM public.table;
            """;

    private static final String FIND_ALL_WHERE_HALL_ID_QUERY = """
            SELECT * FROM public.table
            WHERE hall_id = :hallId;
            """;
    private static final String FIND_BY_ID_QUERY = """
            SELECT * FROM public."table"
            WHERE id = :id;
            """;

    private static final String DELETE_QUERY = """
            DELETE FROM public."table"
            WHERE id = :id;
            """;

    private static final String SAVE_QUERY = """
            INSERT INTO public."table" (name, capacity, table_type, hall_id, order_id, created_at, deleted_at, active)
            VALUES (:name, :capacity, :tableType, :hallId, :orderId, :createdAt, :deletedAt, :active);
            """;

    private static final String UPDATE_QUERY = """
            UPDATE public."table"
            SET name = :name,
                capacity = :capacity,
                table_type = :tableType,
                hall_id = :hallId,
                order_id = :orderId,
                active = :active,
                created_at = :createdAt,
                deleted_at = :deletedAt
            WHERE id = :id;
            """;

    private static final String FIND_WAITERS_BY_TABLE_ID = """
            SELECT waiter_id FROM public.table_waiter
            WHERE table_id = :tableId;
            """;

    private static final String DELETE_WAITERS_QUERY = """
            DELETE FROM public.table_waiter
            WHERE table_id = :tableId;
            """;

    private static final String SAVE_WAITER_QUERY = """
            INSERT INTO public.table_waiter (table_id, waiter_id)
            VALUES (:tableId, :waiterId);
            """;

    @Override
    public Table save(Table table) {
        if (nonNull(table.getId())) {
            return update(table);
        }

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(table);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                SAVE_QUERY,
                parameterSource,
                keyHolder
        );

        UUID generatedId = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        table.setId(generatedId);

        updateTableWaiters(table);

        return table;
    }

    @Override
    public void delete(Table table) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", table.getId());
        jdbcTemplate.update(
                DELETE_QUERY,
                parameterSource
        );
    }

    @Override
    public Optional<Table> findById(UUID id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        Table table = jdbcTemplate.query(
                FIND_BY_ID_QUERY,
                parameterSource,
                tableRowMapper
        ).stream().findFirst().orElse(null);

        if (nonNull(table)) {
            table.setWaitersId(findWaitersByTableId(table.getId()));
        }

        return Optional.ofNullable(table);
    }

    @Override
    public List<Table> findAll() {
        List<Table> tables = jdbcTemplate.query(
                FIND_ALL_QUERY,
                tableRowMapper
        );

        for (Table table : tables) {
            table.setWaitersId(findWaitersByTableId(table.getId()));
        }

        return tables;
    }

    @Override
    public List<Table> findAllWhereHallId(UUID hallId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("hallId", hallId);
        return jdbcTemplate.query(
                FIND_ALL_WHERE_HALL_ID_QUERY,
                parameterSource,
                tableRowMapper
        );
    }

    private List<UUID> findWaitersByTableId(UUID tableId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("tableId", tableId);
        return jdbcTemplate.queryForList(
                FIND_WAITERS_BY_TABLE_ID,
                parameterSource,
                UUID.class
        );
    }

    private Table update(Table table) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(table);
        jdbcTemplate.update(
                UPDATE_QUERY,
                parameterSource
        );

        updateTableWaiters(table);

        return table;
    }

    private void updateTableWaiters(Table table) {
        deleteTableWaiters(table.getId());
        saveTableWaiters(table.getId(), table.getWaitersId());
    }

    private void deleteTableWaiters(UUID tableId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("tableId", tableId);
        jdbcTemplate.update(
                DELETE_WAITERS_QUERY,
                parameterSource
        );
    }

    private void saveTableWaiters(UUID tableId, List<UUID> waitersId) {
        waitersId.forEach(waiterId -> {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("tableId", tableId)
                    .addValue("waiterId", waiterId);
            jdbcTemplate.update(
                    SAVE_WAITER_QUERY,
                    parameterSource
            );
        });
    }
}
