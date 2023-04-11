package com.konstantion.product.model;

import java.util.UUID;

public record GetProductsRequest(
        int pageNumber,
        int pageSize,
        String orderBy,
        String searchPattern,
        UUID categoryId,
        boolean ascending
) {
}
