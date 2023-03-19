package com.konstantion.category;

import com.konstantion.category.dto.CategoryDto;
import com.konstantion.category.dto.CreationCategoryDto;
import com.konstantion.category.dto.UpdateCategoryDto;
import com.konstantion.category.validator.CategoryValidator;
import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;

public record CategoryServiceImp(
        CategoryRepository categoryRepository,
        CategoryValidator categoryValidator
) implements CategoryService {

    private static final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImp.class);

    @Override
    public CategoryDto getCategoryById(UUID uuid) {
        Category category = getCategoryByIdOrThrow(uuid);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto createCategory(CreationCategoryDto creationDto) {
        ValidationResult validationResult = categoryValidator.validate(creationDto);
        if (validationResult.errorsPresent()) {
            throw new ValidationException("Failed to create category, given data is invalid",
                    validationResult.getErrorsAsMap()
            );
        }

        Category category = Category.builder()
                .name(creationDto.name())
                .createdAt(now())
                .userUuid(null)
                .build();

        category = categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto updateCategory(UUID categoryId, UpdateCategoryDto updateDto) {
        Category category = getCategoryByIdOrThrow(categoryId);

        ValidationResult validationResult = categoryValidator.validate(updateDto);
        if (validationResult.errorsPresent()) {
            throw new ValidationException("Failed to update category, given data is invalid",
                    validationResult.getErrorsAsMap()
            );
        }

        category = updateCategory(category, updateDto);

        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto deleteCategory(UUID categoryId) {
        Category category = getCategoryByIdOrThrow(categoryId);
        categoryRepository.delete(category);

        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getCategories() {
        return categoryMapper.toDto(categoryRepository.findAll());
    }

    private Category getCategoryByIdOrThrow(UUID uuid) {
        if (uuid == null) {
            throw new BadRequestException("Category id shouldn't be null");
        }
        return categoryRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Category with uuid %s doesn't exist", uuid)
        ));
    }

    private Category updateCategory(Category target, UpdateCategoryDto updateDto) {
        return target.setName(updateDto.name());
    }
}
