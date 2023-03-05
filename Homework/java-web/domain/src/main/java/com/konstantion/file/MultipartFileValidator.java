package com.konstantion.file;

import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component
public record MultipartFileValidator(MultipartFileValidations fileValidations) {
    public ValidationResult validate(MultipartFile file) {
        Set<FieldError> validationErrors = new HashSet<>();
        fileValidations.isFileValid(file).ifPresent(
                s -> validationErrors.add(new FieldError(
                        file.toString(), "file", s)
                )
        );

        return ValidationResult.ofAbsentee(validationErrors);
    }
}
