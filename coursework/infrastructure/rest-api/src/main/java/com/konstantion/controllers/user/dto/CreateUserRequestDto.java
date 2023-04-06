package com.konstantion.controllers.user.dto;

import java.util.UUID;

public record CreateUserRequestDto(
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
