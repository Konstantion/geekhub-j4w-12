package com.konstantion.adapters.product;

import com.konstantion.product.Product;
import com.konstantion.product.ProductPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public record ProductDatabaseAdapter() implements ProductPort {
    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public void delete(Product product) {

    }

    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.empty();
    }
}
