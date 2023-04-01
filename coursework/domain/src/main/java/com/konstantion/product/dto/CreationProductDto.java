package com.konstantion.product.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreationProductDto(
        String name,
        Double price,
        Double weight,
        MultipartFile image,
        String description,
        UUID categoryId
) {
}
