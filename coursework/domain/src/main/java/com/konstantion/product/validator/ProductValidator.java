package com.konstantion.product.validator;

import com.konstantion.product.Product;
import com.konstantion.product.model.CreateProductRequest;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

@Component
public record ProductValidator(
        ProductValidations productValidations
) {
    public ValidationResult validate(Product product) {
        Set<FieldError> validationErrors = new HashSet<>();
        return ValidationResult.of(validationErrors);
    }

    public ValidationResult validate(CreateProductRequest createProductRequest) {
        Set<FieldError> validationErrors = new HashSet<>();
        return ValidationResult.of(validationErrors);
    }
}
