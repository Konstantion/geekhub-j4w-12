package com.konstantion.category;

import com.konstantion.category.model.CreateCategoryRequest;
import com.konstantion.category.model.UpdateCategoryRequest;
import com.konstantion.category.validator.CategoryValidator;
import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;

@Component
public record CategoryServiceImpl(
        CategoryPort categoryPort,
        CategoryValidator categoryValidator
) implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public List<Category> getAll() {
        return categoryPort.findAll();
    }

    @Override
    public Category create(CreateCategoryRequest request, User user) {
        categoryValidator.validate(request).validOrTrow();

        Category category = Category.builder()
                .name(request.name())
                .creatorId(user.getId())
                .build();

        categoryPort.save(category);

        return category;
    }

    @Override
    public Category deleteById(UUID id) {
        Category category = getByIdOrThrow(id);

        categoryPort.delete(category);

        return category;
    }

    @Override
    public Category update(UUID id, UpdateCategoryRequest request) {
        categoryValidator.validate(request).validOrTrow();

        Category category = getByIdOrThrow(id);

        updateCategory(category, request);

        categoryPort.save(category);

        return category;
    }

    private void updateCategory(Category category, UpdateCategoryRequest request) {
        category.setName(request.name());
    }

    @Override
    public Category getById(UUID id) {
        return getByIdOrThrow(id);
    }

    private Category getByIdOrThrow(UUID id) {
        return categoryPort.findById(id)
                .orElseThrow(nonExistingIdSupplier(Category.class, id));
    }
}
