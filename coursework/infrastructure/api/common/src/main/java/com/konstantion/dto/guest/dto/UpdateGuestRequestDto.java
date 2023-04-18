package com.konstantion.dto.guest.dto;

public record UpdateGuestRequestDto(
        String name,
        String phoneNumber,
        Double discountPercent
) {
}
