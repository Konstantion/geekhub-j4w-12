package com.konstantion.product.validator;

import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.UpdateProductDto;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

@Component
public record ProductValidator(ProductValidations productValidations) {
    public ValidationResult validate(CreationProductDto creationProductDto) {
        Set<FieldError> validationErrors = new HashSet<>();
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

        productValidations.isDescriptionValid(creationProductDto.description()).ifPresent(
                s -> validationErrors.add(new FieldError(
                        creationProductDto.toString(), "description", s)
                )
        );

        return ValidationResult.ofAbsentee(validationErrors);
    }

    public ValidationResult validate(UpdateProductDto updateProductDto) {
        Set<FieldError> validationErrors = new HashSet<>();
        productValidations.isNameValid(updateProductDto.name()).ifPresent(
                s -> validationErrors.add(new FieldError(
                        updateProductDto.toString(), "name", s)
                )
        );

        productValidations.isPriceValid(updateProductDto.price()).ifPresent(
                s -> validationErrors.add(new FieldError(
                        updateProductDto.toString(), "price", s)
                )
        );

        productValidations.isDescriptionValid(updateProductDto.description()).ifPresent(
                s -> validationErrors.add(new FieldError(
                        updateProductDto.toString(), "description", s)
                )
        );

        return ValidationResult.ofAbsentee(validationErrors);
    }
}
