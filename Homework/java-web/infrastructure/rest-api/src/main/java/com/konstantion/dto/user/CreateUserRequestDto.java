package com.konstantion.dto.user;

public record CreateUserRequestDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String password
) {
}
