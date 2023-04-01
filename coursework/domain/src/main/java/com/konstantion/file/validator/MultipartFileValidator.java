package com.konstantion.file.validator;

import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Component
public record MultipartFileValidator(
        MultipartFileValidations validations
) {
    public ValidationResult validate(MultipartFile multipartFile) {
        Set<FieldError> errors = new HashSet<>();

        validations.isFileValid(multipartFile).ifPresent(
                (errors::add)
        );

        return ValidationResult.of(errors);
    }
}
