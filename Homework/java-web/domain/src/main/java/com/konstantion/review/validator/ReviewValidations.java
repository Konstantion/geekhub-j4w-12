package com.konstantion.review.validator;

import com.konstantion.utils.validator.ValidationsUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public record ReviewValidations() implements ValidationsUtil {
    public Optional<String> isMessageValid(String message) {
        List<String> errorList = new ArrayList<>();
        if (isNull(message)) {
            return Optional.of("Message shouldn't be null");
        }

        if (isBlank(message)) {
            return Optional.of("Message shouldn't be empty");
        }

        if (message.length() > 255) {
            errorList.add("Message should be less or equal to 255 characters");
        }

        return listToOptionalString(errorList);
    }

    public Optional<String> isRatingValid(Integer rating) {
        if (isNull(rating)) {
            return Optional.of("Message shouldn't be null");
        }

        if (rating < 0 || rating > 5) {
            return Optional.of("Rating should be between 0 and 5");
        }

        return Optional.empty();
    }
}
