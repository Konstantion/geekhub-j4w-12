package com.konstantion.user.validator;

import com.konstantion.user.model.CreateUserRequest;
import com.konstantion.user.model.LoginUserRequest;
import com.konstantion.user.model.UpdateUserRequest;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

@Component
public record UserValidator(
  UserValidations userValidations
) {
    public ValidationResult validate(CreateUserRequest createUserRequest) {
        Set<FieldError> validationErrors = new HashSet<>();
        return ValidationResult.of(validationErrors);
    }

    public ValidationResult validate(LoginUserRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();
        return ValidationResult.of(validationErrors);
    }

    public ValidationResult validate(UpdateUserRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();
        return ValidationResult.of(validationErrors);
    }
}
