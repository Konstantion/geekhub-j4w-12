package com.konstantion.guest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record GuestDto(
        UUID id,
        String name,
        String phoneNumber,
        Double discountPercent,
        LocalDateTime createdAt,
        Double totalSpentSum,
        Boolean active
) {
    public boolean isActive() {
        return active;
    }
}
