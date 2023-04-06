package com.konstantion.guest.model;

public record CreateGuestRequest(
        String name,
        String phoneNumber,
        Double discountPercent
) {
}
