package com.konstantion.product.model;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UpdateProductRequest(
        @Nullable String name,
        @Nullable Double price,
        @Nullable String description,
        @Nullable UUID categoryUuid,
        @Nullable MultipartFile file
) {
}
