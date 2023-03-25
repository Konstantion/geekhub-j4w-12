package com.konstantion.user.dto;

public record RegistrationUserDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String password,
        String passwordConfirm
) {
}
