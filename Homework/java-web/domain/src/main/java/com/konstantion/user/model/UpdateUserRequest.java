package com.konstantion.user.model;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String password
) {
}
