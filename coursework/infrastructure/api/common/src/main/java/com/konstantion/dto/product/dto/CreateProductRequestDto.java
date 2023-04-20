package com.konstantion.dto.product.dto;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreateProductRequestDto(
        String name,
        Double price,
        Double weight,
        @Nullable MultipartFile image,
        String description,
        UUID categoryId
) {
}
