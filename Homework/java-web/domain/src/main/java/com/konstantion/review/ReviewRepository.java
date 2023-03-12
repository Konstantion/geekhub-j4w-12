package com.konstantion.review;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    Optional<Review> findById(UUID uuid);

    Review save(Review review);

    void delete(Review review);

    void deleteById(UUID uuid);
}
