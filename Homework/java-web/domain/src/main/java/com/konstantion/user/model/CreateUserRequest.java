package com.konstantion.user.model;

public record CreateUserRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String password
) {
}
