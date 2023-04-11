package com.konstantion.call.model;

import com.konstantion.call.Purpose;

import java.util.UUID;

public record CreateCallRequest(
        Purpose purpose,
        UUID tableId
) {
}
