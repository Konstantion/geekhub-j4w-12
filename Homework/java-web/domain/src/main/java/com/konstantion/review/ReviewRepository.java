package com.konstantion.review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    Optional<Review> findById(UUID uuid);

    List<Review> findByUserId(UUID uuid);

    List<Review> findByProductId(UUID uuid);

    Review save(Review review);

    void delete(Review review);

    void deleteById(UUID uuid);
}
