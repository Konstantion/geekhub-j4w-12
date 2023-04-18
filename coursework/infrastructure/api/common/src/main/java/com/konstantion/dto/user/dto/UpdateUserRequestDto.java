package com.konstantion.dto.user.dto;

public record UpdateUserRequestDto(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Integer age,
        String password
) {
}
