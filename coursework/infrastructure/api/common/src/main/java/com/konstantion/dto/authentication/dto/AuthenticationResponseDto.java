package com.konstantion.dto.authentication.dto;

import java.util.Map;

public record AuthenticationResponseDto(
        String token,
        Map<String, Object> userDetails
) {
}
