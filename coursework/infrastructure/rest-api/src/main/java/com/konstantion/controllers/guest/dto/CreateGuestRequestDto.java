package com.konstantion.controllers.guest.dto;

public record CreateGuestRequestDto(
        String name,
        String phoneNumber,
        Double discountPercent
) {


}
