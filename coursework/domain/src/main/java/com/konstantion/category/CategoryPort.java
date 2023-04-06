package com.konstantion.category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryPort {
    Optional<Category> findById(UUID id);
}
