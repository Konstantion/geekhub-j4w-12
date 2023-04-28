package com.konstantion.dto.bill.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BillDto(
        UUID id,
        UUID waiterId,
        UUID orderId,
        UUID guestId,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime closedAt,
        Double price,
        Double priceWithDiscount
) {
}
