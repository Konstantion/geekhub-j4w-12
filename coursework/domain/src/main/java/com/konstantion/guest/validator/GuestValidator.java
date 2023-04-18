package com.konstantion.guest.validator;

import com.konstantion.guest.model.CreateGuestRequest;
import com.konstantion.guest.model.UpdateGuestRequest;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

@Component
public record GuestValidator(
        GuestValidation guestValidation
) {
    public ValidationResult validate(CreateGuestRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();
        return ValidationResult.of(validationErrors);
    }

    public ValidationResult validate(UpdateGuestRequest request) {
        Set<FieldError> validationErrors = new HashSet<>();
        return ValidationResult.of(validationErrors);
    }
}
