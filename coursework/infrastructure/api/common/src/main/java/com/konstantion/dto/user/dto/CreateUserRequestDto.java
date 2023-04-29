package com.konstantion.dto.user.dto;

public record CreateUserRequestDto(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Integer age,
        String password,
        String passwordCopy
) {
}
