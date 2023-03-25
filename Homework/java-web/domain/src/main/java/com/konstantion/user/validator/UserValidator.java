package com.konstantion.user.validator;

import com.konstantion.user.dto.LoginUserDto;
import com.konstantion.user.dto.RegistrationUserDto;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

@Component
public record UserValidator(UserValidations userValidations) {
    public ValidationResult validate(RegistrationUserDto registrationUserDto) {
        Set<FieldError> validationErrors = new HashSet<>();

        return ValidationResult.ofAbsentee(validationErrors);
    }

    public ValidationResult validate(LoginUserDto loginUserDto) {
        Set<FieldError> validationErrors = new HashSet<>();
        userValidations.isLoginEmailValid(loginUserDto.email())
                .ifPresent(s ->
                        validationErrors.add(new FieldError(
                                loginUserDto.toString(), "email", s)
                        ));

        userValidations.isLoginPasswordValid(loginUserDto.password())
                .ifPresent(s ->
                        validationErrors.add(new FieldError(
                                loginUserDto.toString(), "password", s)
                        ));
        return ValidationResult.ofAbsentee(validationErrors);
    }
}
