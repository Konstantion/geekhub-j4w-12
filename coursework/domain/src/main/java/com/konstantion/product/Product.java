package com.konstantion.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Product {
    private UUID id;
    private String name;
    private Double price;
    private Double weight;
    private UUID categoryId;
    private byte[] imageBytes;
    private String description;
    private UUID creatorId;
    private LocalDateTime createdAt;
    private LocalDateTime deactivateAt;
    private Boolean active;

    public Product() {
    }

    public boolean isActive() {
        return active;
    }
}
