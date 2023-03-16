package com.konstantion.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    List<Category> findAll();
    Optional<Category> findById(UUID uuid);

    Category save(Category category);

    void delete(Category category);

    void deleteById(UUID uuid);
}
