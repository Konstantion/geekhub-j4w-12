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
    public ValidationResult validate(CreateUserRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();

        userValidations.isFirstNameValid(request.firstName(), request)
                .ifPresent(validationErrors::add);

        userValidations.isLastNameValid(request.lastName(), request)
                .ifPresent(validationErrors::add);

        userValidations.isEmailValid(request.email(), request)
                .ifPresent(validationErrors::add);

        userValidations.isPhoneNumberValid(request.phoneNumber(), request)
                .ifPresent(validationErrors::add);

        userValidations.isAgeValid(request.age(), request)
                .ifPresent(validationErrors::add);

        userValidations.isPasswordValid(request.password(), request)
                .ifPresent(validationErrors::add);

        userValidations.isPasswordCopyValid(request.password(), request.passwordCopy(), request)
                .ifPresent(validationErrors::add);

        return ValidationResult.of(validationErrors);
    }

    public ValidationResult validate(LoginUserRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();

        userValidations.isLoginPasswordValid(request.password(), request)
                .ifPresent(validationErrors::add);
        return ValidationResult.of(validationErrors);
    }

    public ValidationResult validate(UpdateUserRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();

        userValidations.isUpdateFirstNameValid(request.firstName(), request)
                .ifPresent(validationErrors::add);

        userValidations.isUpdateLastNameValid(request.lastName(), request)
                .ifPresent(validationErrors::add);

        userValidations.isUpdateEmailValid(request.email(), request)
                .ifPresent(validationErrors::add);

        userValidations.isUpdatePhoneNumberValid(request.phoneNumber(), request)
                .ifPresent(validationErrors::add);

        userValidations.isUpdateAgeValid(request.age(), request)
                .ifPresent(validationErrors::add);

        userValidations.isUpdatePasswordValid(request.password(), request)
                .ifPresent(validationErrors::add);

        return ValidationResult.of(validationErrors);
    }

    public ValidationResult validateAdmin(CreateUserRequest request) {
        Set<FieldError> errors = new HashSet<>();

        userValidations.isAdminEmailValid(request.email(), request)
                .ifPresent(errors::add);

        userValidations.isPasswordValid(request.password(), request)
                .ifPresent(errors::add);

        userValidations.isPasswordCopyValid(request.password(), request.passwordCopy(), request)
                .ifPresent(errors::add);

        return ValidationResult.of(errors);
    }
}
