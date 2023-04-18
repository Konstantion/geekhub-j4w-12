package com.konstantion.dto.product.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UpdateProductRequestDto(
        String name,
        Double price,
        Double weight,
        MultipartFile image,
        String description,
        UUID categoryId
) {
}
