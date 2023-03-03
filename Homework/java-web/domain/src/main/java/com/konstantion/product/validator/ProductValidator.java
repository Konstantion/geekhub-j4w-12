package com.konstantion.product.validator;

import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Component
public record ProductValidator(ProductValidations productValidations) {
    public ValidationResult validate(CreationProductDto creationProductDto) {
        List<FieldError> validationErrors = new ArrayList<>();
        productValidations.isNameValid(creationProductDto.name()).ifPresent(
                s -> validationErrors.add(new FieldError(
                        creationProductDto.toString(), "name", s)
                )
        );

        productValidations.isPriceValid(creationProductDto.price()).ifPresent(
                s -> validationErrors.add(new FieldError(
                        creationProductDto.toString(), "price", s)
                )
        );

        if (validationErrors.isEmpty()) {
            return ValidationResult.empty();
        }

        return ValidationResult.of(validationErrors);
    }
}
