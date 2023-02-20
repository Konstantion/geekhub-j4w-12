package com.konstantion.utils.validator;

import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ValidationResult(List<FieldError> fieldErrors,boolean isErrorsPresent) {

    public static ValidationResult empty() {
        return new ValidationResult(Collections.emptyList(), false);
    }

    public static ValidationResult of(List<FieldError> fieldErrors) {
        return new ValidationResult(fieldErrors, true);
    }

    public Map<String, String> getErrorsAsMap() {
        return fieldErrors.stream().collect(
                Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                )
        );
    }
}
