package com.konstantion.dto.call.dto;

import com.konstantion.call.Purpose;

import java.util.UUID;

public record CreateCallRequestDto(
        Purpose purpose,
        UUID tableId
) {
}
