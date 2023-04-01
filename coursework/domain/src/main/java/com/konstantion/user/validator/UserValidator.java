package com.konstantion.user.validator;

import com.konstantion.user.dto.CreationUserDto;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

@Component
public record UserValidator(
  UserValidations userValidations
) {
    public ValidationResult validate(CreationUserDto cudto) {
        Set<FieldError> validationErrors = new HashSet<>();
        return ValidationResult.of(validationErrors);
    }
}
