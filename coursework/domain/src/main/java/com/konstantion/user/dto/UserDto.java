package com.konstantion.user.dto;

import com.konstantion.user.Permission;
import com.konstantion.user.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserDto(
        UUID uuid,
        String firstName,
        String lastName,
        String username,
        String phoneNumber,
        Integer age,
        String password,
        Boolean active,
        Set<Role> roles,
        LocalDateTime createdAt,
        Set<Permission> permissions
) {
}
