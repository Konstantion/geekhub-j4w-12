package com.konstantion.review.validator;

import com.konstantion.review.dto.CreationReviewDto;
import com.konstantion.utils.validator.ValidationResult;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@C
public record ReviewValidator(ReviewValidations reviewValidations) {
    public ValidationResult validate(CreationReviewDto creationReviewDto) {
        Set<FieldError> validationErrors = new HashSet<>();
        reviewValidations.isMessageValid(creationReviewDto.message()).ifPresent(
                s -> validationErrors.add(new FieldError(
                        creationReviewDto.toString(), "message", s)
                )
        );

        reviewValidations.isRatingValid(creationReviewDto.rating()).ifPresent(
                s -> validationErrors.add(new FieldError(
                        creationReviewDto.toString(), "rating", s)
                )
        );

        if (validationErrors.isEmpty()) {
            return ValidationResult.empty();
        }

        return ValidationResult.of(validationErrors);
    }
}
