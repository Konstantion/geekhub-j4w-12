package com.konstantion.utils.validator;

import org.springframework.validation.FieldError;

import java.util.Optional;
import java.util.Set;

public interface ValidationUtil {
    default Optional<FieldError> setToOptional(Set<String> validations, Object object, String field) {
        if (validations.isEmpty()) {
            return Optional.empty();
        }

        String data = String.join(
                System.lineSeparator(),
                validations
        );

        if (data.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(new FieldError(object.toString(), field, data));
    }
}
