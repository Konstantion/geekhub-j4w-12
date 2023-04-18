package com.konstantion.dto.order.dto;

import java.util.UUID;

public record OrderProductsRequestDto(
        UUID productId,
        int quantity
) {
}
