package com.konstantion.user.validator;

import com.konstantion.user.model.CreateUserRequest;
import com.konstantion.user.model.LoginUserRequest;
import com.konstantion.user.model.RegistrationUserRequest;
import com.konstantion.user.model.UpdateUserRequest;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

@Component
public record UserValidator(UserValidations userValidations) {
    public ValidationResult validate(RegistrationUserRequest registrationUserRequest) {
        Set<FieldError> validationErrors = new HashSet<>();

        userValidations.isEmailValid(registrationUserRequest.email()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        registrationUserRequest.toString(), "email", s)
                ));

        userValidations.isFirstnameValid(registrationUserRequest.firstName()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        registrationUserRequest.toString(), "firstName", s)
                ));

        userValidations.isLastnameValid(registrationUserRequest.lastName()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        registrationUserRequest.toString(), "lastName", s)
                ));

        userValidations.isPasswordValid(registrationUserRequest.password()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        registrationUserRequest.toString(), "password", s)
                ));

        userValidations.isPasswordConfirmValid(registrationUserRequest.password(), registrationUserRequest.passwordConfirm()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        registrationUserRequest.toString(), "passwordConfirm", s)
                ));

        return ValidationResult.ofAbsentee(validationErrors);
    }

    public ValidationResult validate(LoginUserRequest loginUserRequest) {
        Set<FieldError> validationErrors = new HashSet<>();
        userValidations.isLoginEmailValid(loginUserRequest.email())
                .ifPresent(s ->
                        validationErrors.add(new FieldError(
                                loginUserRequest.toString(), "email", s)
                        ));

        userValidations.isLoginPasswordValid(loginUserRequest.password())
                .ifPresent(s ->
                        validationErrors.add(new FieldError(
                                loginUserRequest.toString(), "password", s)
                        ));
        return ValidationResult.ofAbsentee(validationErrors);
    }

    public ValidationResult validate(UpdateUserRequest updateUserRequest) {
        Set<FieldError> validationErrors = new HashSet<>();

        return ValidationResult.ofAbsentee(validationErrors);
    }

    public ValidationResult validate(CreateUserRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();

        return ValidationResult.ofAbsentee(validationErrors);
    }
}
