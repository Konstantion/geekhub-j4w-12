package com.konstantion.category;

import com.konstantion.category.dto.CategoryDto;
import com.konstantion.category.dto.CreationCategoryDto;
import com.konstantion.category.dto.UpdateCategoryDto;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDto getCategoryById(UUID uuid);

    CategoryDto createCategory(CreationCategoryDto creationDto, User user);

    CategoryDto updateCategory(UUID categoryId, UpdateCategoryDto updateDto, User user);

    CategoryDto deleteCategory(UUID categoryId, User user);

    List<CategoryDto> getCategories();
}
