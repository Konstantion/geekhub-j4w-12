package com.konstantion.product;

import com.konstantion.review.Review;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Optional<Product> findById(Long id);

    Optional<Product> findByUuid(UUID uuid);

    List<Product> findAll();

    List<Product> findAll(Sort sort);

    Product save(Product product);

    void delete(Product product);

    void deleteById(Long id);

    void deleteByUuid(UUID uuid);

    Map<Product, List<Review>> findAllProductsAndReview();

    default Product saveAndFlush(Product product) {
        return save(product);
    }
}
