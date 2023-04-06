package com.konstantion.controllers.product.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreateProductRequestDto(
        String name,
        Double price,
        Double weight,
        MultipartFile image,
        String description,
        UUID categoryId
) {
}
