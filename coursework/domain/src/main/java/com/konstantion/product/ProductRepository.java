package com.konstantion.product;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository {
    Product save(Product product);

    void delete(Product product);

    Optional<Product> findById(UUID id);


}
