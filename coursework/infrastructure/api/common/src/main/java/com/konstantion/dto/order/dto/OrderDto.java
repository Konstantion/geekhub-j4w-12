package com.konstantion.dto.order.dto;

import java.util.List;
import java.util.UUID;

public record OrderDto(
        UUID id,
        Boolean active,
        List<UUID> productsId
) {
}
