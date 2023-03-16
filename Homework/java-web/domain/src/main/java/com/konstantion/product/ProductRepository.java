package com.konstantion.product;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Optional<Product> findById(UUID uuid);

    List<Product> findAll();

    List<Product> findAll(Sort sort);

    Product save(Product product);

    void delete(Product product);

    void deleteById(UUID uuid);

    default Product saveAndFlush(Product product) {
        return save(product);
    }
}
