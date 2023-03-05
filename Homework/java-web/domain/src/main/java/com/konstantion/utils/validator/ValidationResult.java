package com.konstantion.utils.validator;

import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record ValidationResult(Set<FieldError> fieldErrors, boolean errorsPresent) {

    public static ValidationResult empty() {
        return new ValidationResult(Collections.emptySet(), false);
    }

    public static ValidationResult of(Set<FieldError> fieldErrors) {
        return new ValidationResult(fieldErrors, true);
    }

    public static ValidationResult ofAbsentee(Set<FieldError> fieldErrors) {
        return new ValidationResult(fieldErrors, !fieldErrors.isEmpty());
    }

    public Map<String, String> getErrorsAsMap() {
        return fieldErrors.stream().collect(
                Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                )
        );
    }

    public ValidationResult combine(ValidationResult that) {
        fieldErrors.addAll(that.fieldErrors());
        return new ValidationResult(fieldErrors, errorsPresent || that.errorsPresent());
    }
}
