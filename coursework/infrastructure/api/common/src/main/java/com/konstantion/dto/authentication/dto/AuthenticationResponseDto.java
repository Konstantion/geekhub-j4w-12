package com.konstantion.dto.authentication.dto;

import java.util.Map;

public record AuthenticationResponseDto<T>(
        String token,
        Map<String, T> authenticated
) {
}
