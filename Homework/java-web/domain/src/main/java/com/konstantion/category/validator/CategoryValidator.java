package com.konstantion.category.validator;

import com.konstantion.category.dto.CreationCategoryDto;
import com.konstantion.category.dto.UpdateCategoryDto;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public record CategoryValidator(CategoryValidations categoryValidations) {
    public ValidationResult validate(CreationCategoryDto categoryDto) {
        return ValidationResult.empty();
    }

    public ValidationResult validate(UpdateCategoryDto categoryDto) {
        return ValidationResult.empty();
    }
}
