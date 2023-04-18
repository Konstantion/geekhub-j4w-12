package com.konstantion.guest.model;

public record UpdateGuestRequest(
        String name,
        String phoneNumber,
        Double discountPercent
) {
}
