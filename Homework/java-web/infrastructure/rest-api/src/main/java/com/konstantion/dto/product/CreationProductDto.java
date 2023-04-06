package com.konstantion.dto.product;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreationProductDto(
        @Nullable String name,
        @Nullable Double price,
        @Nullable String description,
        @Nullable UUID categoryUuid,
        MultipartFile file) {
}
