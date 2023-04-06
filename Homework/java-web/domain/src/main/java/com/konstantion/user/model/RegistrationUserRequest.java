package com.konstantion.user.model;

public record RegistrationUserRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String password,
        String passwordConfirm
) {
}
