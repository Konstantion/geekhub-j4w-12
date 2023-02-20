package com.konstantion.product;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);

    List<Product> findAll();

    List<Product> findAll(Sort sort);

    Product save(Product product);

    void delete(Product product);

    void deleteById(Long id);

    default Product saveAndFlush(Product product) {
        return save(product);
    }
}
