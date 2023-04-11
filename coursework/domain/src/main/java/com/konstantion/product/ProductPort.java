package com.konstantion.product;

import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface ProductPort {
    Product save(Product product);

    void delete(Product product);

    Optional<Product> findById(UUID id);

    Page<Product> findAll(int pageNumber, int pageSize, String orderBy, String searchPattern, UUID categoryId, boolean ascending);
}
