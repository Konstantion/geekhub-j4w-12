package com.konstantion.user.dto;

import com.konstantion.user.Role;

import java.util.Set;
import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        Set<Role> roles
) {
}
