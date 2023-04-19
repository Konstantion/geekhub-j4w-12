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
        GuestValidations guestValidation
) {
    public ValidationResult validate(CreateGuestRequest request) {
        Set<FieldError> errors = new HashSet<>();

        guestValidation.isNameValid(request.name(), request)
                .ifPresent(errors::add);

        guestValidation.isDiscountValid(request.discountPercent(), request)
                .ifPresent(errors::add);

        guestValidation.isPhoneNumberValid(request.phoneNumber(), request)
                .ifPresent(errors::add);

        return ValidationResult.of(errors);
    }

    public ValidationResult validate(UpdateGuestRequest request) {
        Set<FieldError> errors = new HashSet<>();

        guestValidation.isUpdateNameValid(request.name(), request)
                .ifPresent(errors::add);

        guestValidation.isUpdateDiscountValid(request.discountPercent(), request)
                .ifPresent(errors::add);

        guestValidation.isUpdatePhoneNumberValid(request.phoneNumber(), request)
                .ifPresent(errors::add);

        return ValidationResult.of(errors);
    }
}
