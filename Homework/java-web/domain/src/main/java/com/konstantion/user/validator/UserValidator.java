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
    public ValidationResult validate(RegistrationUserRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();

        userValidations.isEmailValid(request.email()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "email", s)
                ));

        userValidations.isFirstnameValid(request.firstName()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "firstName", s)
                ));

        userValidations.isLastnameValid(request.lastName()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "lastName", s)
                ));

        userValidations.isPasswordValid(request.password()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "password", s)
                ));

        userValidations.isPasswordConfirmValid(request.password(), request.passwordConfirm()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "passwordConfirm", s)
                ));

        userValidations.isPhoneNumberValid(request.phoneNumber()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "phoneNumber", s)
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

    public ValidationResult validate(UpdateUserRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();

        userValidations.isFirstnameValid(request.firstName()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "firstName", s)
                ));

        userValidations.isLastnameValid(request.lastName()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "lastName", s)
                ));

        userValidations.isUpdatePasswordValid(request.password()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "password", s)
        ));

        userValidations.isPhoneNumberValid(request.phoneNumber()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "phoneNumber", s)
                ));

        return ValidationResult.ofAbsentee(validationErrors);
    }

    public ValidationResult validate(CreateUserRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();

        userValidations.isEmailValid(request.email()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "email", s)
                ));

        userValidations.isFirstnameValid(request.firstName()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "firstName", s)
                ));

        userValidations.isLastnameValid(request.lastName()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "lastName", s)
                ));

        userValidations.isPasswordValid(request.password()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "password", s)
                ));

        userValidations.isPhoneNumberValid(request.phoneNumber()).ifPresent(s ->
                validationErrors.add(new FieldError(
                        request.toString(), "phoneNumber", s)
                ));

        return ValidationResult.ofAbsentee(validationErrors);
    }
}
