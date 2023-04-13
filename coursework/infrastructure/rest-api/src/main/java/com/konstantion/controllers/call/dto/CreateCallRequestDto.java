package com.konstantion.controllers.call.dto;

import com.konstantion.call.Purpose;

import java.util.UUID;

public record CreateCallRequestDto(
        Purpose purpose,
        UUID tableId
) {
}
