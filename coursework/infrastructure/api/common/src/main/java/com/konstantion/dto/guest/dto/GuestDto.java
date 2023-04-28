package com.konstantion.dto.guest.dto;

import java.util.UUID;

public record GuestDto(
        UUID id,
        String name,
        String phoneNumber,
        Double discountPercent,
        Double totalSpentSum,
        Boolean active
) {
    public boolean isActive() {
        return active;
    }
}
