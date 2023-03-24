package com.konstantion.reporitories.mappers;

import com.konstantion.ragistration.token.ConfirmationToken;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Component
public record ConfirmationTokenRowMapper() implements RowMapper<ConfirmationToken> {
    @Override
    public ConfirmationToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ConfirmationToken.builder()
                .id(rs.getObject("uuid", UUID.class))
                .token(rs.getString("token"))
                .createdAt(toLocalDateTimeNullSave(rs.getTimestamp("created_at")))
                .expiresAt(toLocalDateTimeNullSave(rs.getTimestamp("expires_at")))
                .confirmedAt(toLocalDateTimeNullSave(rs.getTimestamp("confirmed_at")))
                .userId(rs.getObject("user_uuid", UUID.class))
                .build();
    }

    private LocalDateTime toLocalDateTimeNullSave(Timestamp timestamp) {
        LocalDateTime localDateTime = null;
        if (nonNull(timestamp)) {
            localDateTime = timestamp.toLocalDateTime();
        }
        return localDateTime;
    }
}
