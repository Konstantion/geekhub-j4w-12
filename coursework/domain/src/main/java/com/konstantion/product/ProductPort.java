package com.konstantion.product;

import java.util.Optional;
import java.util.UUID;

public interface ProductPort {
    Product save(Product product);

    void delete(Product product);

    Optional<Product> findById(UUID id);


}
