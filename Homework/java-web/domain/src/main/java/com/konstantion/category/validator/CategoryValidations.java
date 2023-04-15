package com.konstantion.category.validator;

import com.konstantion.utils.validator.ValidationsUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public record CategoryValidations() implements ValidationsUtil {
    public Optional<String> isNameValid(String name) {

        if (name.isBlank()) {
            return Optional.of("name shouldn't be empty");
        }

        if (name.length() > 64) {
            return Optional.of("name length should be less then 64 characters");
        }

        return Optional.empty();
    }
}
