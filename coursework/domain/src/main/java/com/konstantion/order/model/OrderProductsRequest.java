package com.konstantion.order.model;

import java.util.UUID;

public record OrderProductsRequest(
        UUID productId,
        int quantity
) {
}
