package com.konstantion.user.model;

public record CreateUserRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Integer age,
        String password,
        String passwordCopy
) {
}
