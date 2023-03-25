package com.konstantion.user.dto;

import com.konstantion.user.Role;

import java.util.Set;

public record UserDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        Set<Role> roles
) {
}
