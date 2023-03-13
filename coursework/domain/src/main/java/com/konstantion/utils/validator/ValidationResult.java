package com.konstantion.utils.validator;

import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNullElse;

public record ValidationResult(Set<FieldError> errors, boolean errorsPresent) {
    public static ValidationResult valid() {
        return new ValidationResult(Collections.emptySet(), false);
    }

    public static ValidationResult invalid(Set<FieldError> errors) {
        return new ValidationResult(errors, true);
    }

    public static ValidationResult of(Set<FieldError> errors) {
        return new ValidationResult(errors, !errors.isEmpty());
    }

    public ValidationResult union(ValidationResult that) {
        errors.addAll(that.errors);
        return new ValidationResult(errors, errorsPresent || that.errorsPresent());
    }

    public Map<String, String> errorsMap() {
        return errors.stream().collect(
                Collectors.toMap(
                        FieldError::getField,
                        fe -> requireNonNullElse(
                                fe.getDefaultMessage(), "Invalid field"
                        )
                )
        );
    }
}
