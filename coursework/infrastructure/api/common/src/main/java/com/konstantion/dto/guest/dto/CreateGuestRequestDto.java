package com.konstantion.dto.guest.dto;

public record CreateGuestRequestDto(
        String name,
        String phoneNumber,
        Double discountPercent
) {


}
