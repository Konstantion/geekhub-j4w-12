package com.konstantion.reporitories.mappers;

import com.konstantion.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public record UserRowMapper() implements RowMapper<User>{
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getObject("uuid", UUID.class))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .phoneNumber(rs.getString("phone_number"))
                .enabled(rs.getBoolean("enabled"))
                .accountNonLocked(rs.getBoolean("non_locked"))
                .build();
    }
}
