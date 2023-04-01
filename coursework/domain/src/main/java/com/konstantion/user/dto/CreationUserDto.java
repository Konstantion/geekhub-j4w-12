package com.konstantion.user.dto;

import java.util.UUID;

public record CreationUserDto(
        UUID uuid,
        String firstName,
        String lastName,
        String username,
        String phoneNumber,
        Integer age,
        String password,
        String passwordCopy
) {
}
