package com.konstantion.utils.validator;

import com.konstantion.exceptions.ValidationException;
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

    public static void validOrThrow(ValidationResult validationResult, String message) {
        if (validationResult.errorsPresent()) {
            throw new ValidationException(message,
                    validationResult.getErrorsAsMap()
            );
        }
    }

    public static void validOrThrow(ValidationResult validationResult) {
       validOrThrow(validationResult, "Process failed, given data is invalid");
    }
}
