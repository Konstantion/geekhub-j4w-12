package com.konstantion.controllers.bill.dto;

import java.util.UUID;

public record CreateBillRequestDto(
        UUID orderId,
        UUID guestId
) {
}
