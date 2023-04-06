package com.konstantion.user.model;

import java.util.UUID;

public record CreateUserRequest(
        UUID uuid,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Integer age,
        String password,
        String passwordCopy
) {
}
