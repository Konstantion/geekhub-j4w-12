package com.konstantion.user.model;

public record LoginUserRequest(
        String email,
        String password
) {
}
