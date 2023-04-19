package com.konstantion.hall.validator;

import com.konstantion.hall.model.CreateHallRequest;
import com.konstantion.hall.model.UpdateHallRequest;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

@Component
public record HallValidator(
        HallValidations validations
) {
    public ValidationResult validate(CreateHallRequest request) {
        Set<FieldError> errors = new HashSet<>();
        validations.isNameValid(request.name(), request)
                .ifPresent(errors::add);
        return ValidationResult.of(errors);
    }

    public ValidationResult validate(UpdateHallRequest request) {
        Set<FieldError> errors = new HashSet<>();
        validations.isUpdateNameValid(request.name(), request)
                .ifPresent(errors::add);
        return ValidationResult.of(errors);
    }
}
