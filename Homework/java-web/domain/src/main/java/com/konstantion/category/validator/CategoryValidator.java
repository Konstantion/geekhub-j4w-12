package com.konstantion.category.validator;

import com.konstantion.category.model.CreationCategoryRequest;
import com.konstantion.category.model.UpdateCategoryRequest;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

@Component
public record CategoryValidator(CategoryValidations categoryValidations) {
    public ValidationResult validate(CreationCategoryRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();

        categoryValidations.isNameValid(request.name()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "name", s)
                ));

        return ValidationResult.ofAbsentee(validationErrors);
    }

    public ValidationResult validate(UpdateCategoryRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();

        categoryValidations.isNameValid(request.name()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "name", s)
                ));

        return ValidationResult.ofAbsentee(validationErrors);
    }
}
