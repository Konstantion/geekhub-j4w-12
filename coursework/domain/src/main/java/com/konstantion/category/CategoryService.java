package com.konstantion.category;

import com.konstantion.category.model.CreateCategoryRequest;
import com.konstantion.category.model.UpdateCategoryRequest;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> getAll();

    Category create(CreateCategoryRequest createCategoryRequest, User user);

    Category deleteById(UUID id, User user);

    Category update(UUID id, UpdateCategoryRequest request, User user);

    Category getById(UUID id);
}
