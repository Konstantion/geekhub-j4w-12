package com.konstantion.review;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    Optional<Review> findByUuid(UUID uuid);

    Review save(Review Review);

    void delete(Review Review);

    void deleteByUuid(UUID uuid);
}
