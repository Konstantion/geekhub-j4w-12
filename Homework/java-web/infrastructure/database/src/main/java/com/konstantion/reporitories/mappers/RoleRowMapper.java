package com.konstantion.reporitories.mappers;

import com.konstantion.user.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Component
public record RoleRowMapper() implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        String roleName = rs.getString("name");
        return Arrays.stream(Role.values()).filter(role -> role.name().equals(roleName)).findAny().orElse(null);
    }
}
