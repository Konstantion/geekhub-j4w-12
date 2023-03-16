package com.konstantion.category;

import com.konstantion.category.dto.CategoryDto;
import com.konstantion.category.dto.CreationCategoryDto;
import com.konstantion.category.dto.UpdateCategoryDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDto getCategoryById(UUID uuid);

    CategoryDto createCategory(CreationCategoryDto creationDto);

    CategoryDto updateCategory(UUID categoryId, UpdateCategoryDto updateDto);

    CategoryDto deleteCategory(UUID categoryId);

    List<CategoryDto> getCategories();
}
