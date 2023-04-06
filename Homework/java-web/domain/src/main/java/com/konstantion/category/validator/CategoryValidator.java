package com.konstantion.category.validator;

import com.konstantion.category.model.CreationCategoryRequest;
import com.konstantion.category.model.UpdateCategoryRequest;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public record CategoryValidator(CategoryValidations categoryValidations) {
    public ValidationResult validate(CreationCategoryRequest categoryDto) {
        return ValidationResult.empty();
    }

    public ValidationResult validate(UpdateCategoryRequest categoryDto) {
        return ValidationResult.empty();
    }
}
