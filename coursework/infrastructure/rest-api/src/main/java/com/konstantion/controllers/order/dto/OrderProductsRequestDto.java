package com.konstantion.controllers.order.dto;

import java.util.UUID;

public record OrderProductsRequestDto(
        UUID productId,
        int quantity
) {
}
