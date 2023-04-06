package com.konstantion.guest.model;

public record EditGuestRequest(
        String name,
        String phoneNumber,
        Double discountPercent
) {
}
