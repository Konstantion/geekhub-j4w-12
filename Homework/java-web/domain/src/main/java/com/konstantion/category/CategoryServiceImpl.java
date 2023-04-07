package com.konstantion.category;

import com.konstantion.category.model.CreationCategoryRequest;
import com.konstantion.category.model.UpdateCategoryRequest;
import com.konstantion.category.validator.CategoryValidator;
import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;

public record CategoryServiceImpl(
        CategoryRepository categoryRepository,
        CategoryValidator categoryValidator
) implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public Category getCategoryById(UUID uuid) {
        return getCategoryByIdOrThrow(uuid);
    }

    @Override
    public Category createCategory(CreationCategoryRequest creationDto, User user) {
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

        return category;
    }

    @Override
    public Category updateCategory(UUID categoryId, UpdateCategoryRequest updateDto) {
        Category category = getCategoryByIdOrThrow(categoryId);

        ValidationResult validationResult = categoryValidator.validate(updateDto);
        if (validationResult.errorsPresent()) {
            throw new ValidationException("Failed to update category, given data is invalid",
                    validationResult.getErrorsAsMap()
            );
        }

        category = updateCategory(category, updateDto);

        categoryRepository.save(category);

        return category;
    }

    @Override
    public Category deleteCategory(UUID categoryId) {
        Category category = getCategoryByIdOrThrow(categoryId);
        categoryRepository.delete(category);

        return category;
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    private Category getCategoryByIdOrThrow(UUID uuid) {
        if (uuid == null) {
            throw new BadRequestException("Category id shouldn't be null");
        }
        return categoryRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Category with uuid %s doesn't exist", uuid)
                ));
    }

    private Category updateCategory(Category target, UpdateCategoryRequest updateDto) {
        return target.setName(updateDto.name());
    }
}
