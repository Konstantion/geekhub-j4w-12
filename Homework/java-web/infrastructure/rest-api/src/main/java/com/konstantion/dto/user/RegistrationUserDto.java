package com.konstantion.dto.user;

public record RegistrationUserDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String password,
        String passwordConfirm
) {
}
