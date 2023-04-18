package com.konstantion.dto.bill.dto;

import java.util.UUID;

public record BillDto(
        UUID id,
        UUID waiterId,
        UUID orderId,
        UUID guestId,
        Boolean active,
        Double price,
        Double priceWithDiscount
) {
}
