package com.konstantion.utils.validator;

import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import static com.konstantion.utils.validator.ValidationConstants.PHONE_NUMBER_FIELD;
import static com.konstantion.utils.validator.ValidationConstants.PHONE_NUMBER_PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

public interface ValidationUtil {
    default Optional<FieldError> setToOptional(Set<String> validations, Object object, String field) {
        if (validations.isEmpty()) {
            return Optional.empty();
        }

        String data = String.join(
                "<br>",
                validations
        );

        if (data.isBlank()) {
            return Optional.empty();
        }

        String objectName = object == null ? "null" : object.getClass().getSimpleName();

        return Optional.of(new FieldError(objectName, field, data));
    }

    default Optional<FieldError> isPhoneNumberValid(String phoneNumber, Object sender) {
        Set<String> violations = new HashSet<>();

        if (isBlank(phoneNumber)) {
            violations.add("phone number shouldn't be empty");
            return setToOptional(violations, sender, PHONE_NUMBER_FIELD);
        }

        if (!Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber)) {
            violations.add("invalid phone number format, should be <xxx-xxx-xxxx>");
        }

        return setToOptional(violations, sender, PHONE_NUMBER_FIELD);
    }
}
