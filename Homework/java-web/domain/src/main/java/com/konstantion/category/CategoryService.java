package com.konstantion.category;

import com.konstantion.category.model.CreationCategoryRequest;
import com.konstantion.category.model.UpdateCategoryRequest;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category getCategoryById(UUID uuid);

    Category createCategory(CreationCategoryRequest creationDto, User user);

    Category updateCategory(UUID categoryId, UpdateCategoryRequest updateDto);

    Category deleteCategory(UUID categoryId);

    List<Category> getCategories();
}
