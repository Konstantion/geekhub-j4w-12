package com.konstantion.user.model;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Integer age,
        String password
) {
}
