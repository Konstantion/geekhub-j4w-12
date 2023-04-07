package com.konstantion.dto.user;

public record UpdateUserDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String password
) {
}
