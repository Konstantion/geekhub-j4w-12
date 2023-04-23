package com.konstantion.product;

import com.google.common.base.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equal(id, product.id) && Objects.equal(name, product.name) && Objects.equal(price, product.price) && Objects.equal(weight, product.weight) && Objects.equal(categoryId, product.categoryId) && Objects.equal(imageBytes, product.imageBytes) && Objects.equal(description, product.description) && Objects.equal(creatorId, product.creatorId) && Objects.equal(createdAt, product.createdAt) && Objects.equal(deactivateAt, product.deactivateAt) && Objects.equal(active, product.active);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, price, weight, categoryId, imageBytes, description, creatorId, createdAt, deactivateAt, active);
    }
}
