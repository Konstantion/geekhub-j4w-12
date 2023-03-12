package com.konstantion.product.dto;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record CreationProductDto(
        @Nullable String name,
        @Nullable Double price,
        @Nullable String description,
        @Nullable MultipartFile file) {
}
