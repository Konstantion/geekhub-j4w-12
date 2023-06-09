package com.konstantion.adapters.call;

import com.konstantion.call.Call;
import com.konstantion.call.CallPort;
import com.konstantion.call.Purpose;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.*;

import static java.util.Objects.nonNull;

@Repository
public class CallDatabaseAdapter implements CallPort {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Call> callRowMapper;

    public CallDatabaseAdapter(NamedParameterJdbcTemplate jdbcTemplate, RowMapper<Call> callRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.callRowMapper = callRowMapper;
    }

    public static final String FIND_BY_ID_QUERY = """
            SELECT * FROM public.call
            WHERE id = :id;
            """;

    public static final String FIND_ALL_QUERY = """
            SELECT * FROM public.call;
            """;

    public static final String SAVE_QUERY = """
            INSERT INTO public.call (table_id, purpose, opened_at)
            VALUES (:tableId, :purpose, :openedAt);
            """;

    public static final String UPDATE_QUERY = """
            UPDATE public.call
               SET table_id = :tableId,
                   purpose = :purpose,
                   opened_at = :openedAt
            WHERE id = :id;
            """;
    public static final String FIND_WAITER_ID_BY_CALL_ID = """
            SELECT waiter_id FROM public.call_waiter
            WHERE call_id = :callId;
            """;

    public static final String DELETE_WAITER_ID_BY_CALL_ID = """
            DELETE FROM public.call_waiter
            WHERE call_id = :callId;
            """;

    public static final String SAVE_WAITER_ID = """
            INSERT INTO public.call_waiter (call_id, waiter_id)
            VALUES (:callId, :waiterId);
            """;

    public static final String DELETE_QUERY = """
            DELETE FROM public.call
            WHERE id = :id;
            """;

    public static final String FIND_BY_TABLE_ID_AND_PURPOSE_QUERY = """
            SELECT * FROM public.call
            WHERE table_id = :tableId
            AND purpose = :purpose;
            """;

    public static final String DELETE_ALL_QUERY = """
            DELETE FROM public.call
            WHERE true;
            """;

    @Override
    public List<Call> findAll() {
        List<Call> calls = jdbcTemplate.query(
                FIND_ALL_QUERY,
                callRowMapper
        );

        calls.forEach(call -> call.setWaitersId(fetchWaitersId(call.getId())));

        return calls;
    }

    @Override
    public Optional<Call> findById(UUID id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        Call call = jdbcTemplate.query(
                FIND_BY_ID_QUERY,
                parameterSource,
                callRowMapper
        ).stream().findFirst().orElse(null);

        if (nonNull(call)) {
            call.setWaitersId(fetchWaitersId(call.getId()));
        }

        return Optional.ofNullable(call);
    }

    @Override
    public Call save(Call call) {
        if (nonNull(call.getId())) {
            return update(call);
        }

        SqlParameterSource parameterSource = getParameterSource(call);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                SAVE_QUERY,
                parameterSource,
                keyHolder
        );

        UUID generatedId = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        call.setId(generatedId);

        saveWaiters(call);

        return call;
    }

    @Override
    public void delete(Call call) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", call.getId());
        jdbcTemplate.update(
                DELETE_QUERY,
                parameterSource
        );
    }

    @Override
    public Optional<Call> findByTableIdAndPurpose(UUID tableId, Purpose purpose) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("tableId", tableId)
                .addValue("purpose", purpose.name());

        Call call = jdbcTemplate.query(
                FIND_BY_TABLE_ID_AND_PURPOSE_QUERY,
                parameterSource,
                callRowMapper
        ).stream().findFirst().orElse(null);

        if (nonNull(call)) {
            call.setWaitersId(fetchWaitersId(call.getId()));
        }

        return Optional.ofNullable(call);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(
                DELETE_ALL_QUERY,
                new MapSqlParameterSource()
        );
    }

    private Call update(Call call) {
        SqlParameterSource parameterSource = getParameterSource(call);

        jdbcTemplate.update(
                UPDATE_QUERY,
                parameterSource
        );

        updateWaitersId(call);

        return call;
    }

    private Set<UUID> fetchWaitersId(UUID callId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("callId", callId);
        return new HashSet<>(jdbcTemplate.queryForList(
                FIND_WAITER_ID_BY_CALL_ID,
                parameterSource,
                UUID.class
        ));
    }

    private void updateWaitersId(Call call) {
        deleteWaitersByCallId(call.getId());
        saveWaiters(call);
    }

    private void deleteWaitersByCallId(UUID callId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("callId", callId);
        jdbcTemplate.update(
                DELETE_WAITER_ID_BY_CALL_ID,
                parameterSource
        );
    }

    private void saveWaiters(Call call) {
        call.getWaitersId().forEach(id -> saveWaiterId(call.getId(), id));
    }

    private void saveWaiterId(UUID callId, UUID waiterId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("callId", callId)
                .addValue("waiterId", waiterId);
        jdbcTemplate.update(
                SAVE_WAITER_ID,
                parameterSource
        );
    }

    private SqlParameterSource getParameterSource(Call call) {
        return new BeanPropertySqlParameterSource(call) {
            @Override
            public int getSqlType(String paramName) {
                if ("purpose".equals(paramName)) {
                    return Types.VARCHAR;
                }
                return super.getSqlType(paramName);
            }
        };
    }
}
