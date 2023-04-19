package com.konstantion.product.validator;

import com.konstantion.utils.validator.ValidationUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.konstantion.utils.validator.ValidationConstants.*;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public record ProductValidations(

) implements ValidationUtil {

    public Optional<FieldError> isNameValid(String name, Object sender) {
        Set<String> violations = new HashSet<>();
        if (isBlank(name)) {
            violations.add("name shouldn't be empty");
            return setToOptional(violations, sender, NAME_FIELD);
        }

        if (name.length() > 32) {
            violations.add("name shouldn't be longer then 32 symbols");
        }

        return setToOptional(violations, sender, NAME_FIELD);
    }

    public Optional<FieldError> isPriceValid(Double price, Object sender) {
        Set<String> violations = new HashSet<>();
        if (isNull(price)) {
            violations.add("price shouldn't be null");
            return setToOptional(violations, sender, PRICE_FIELD);
        }

        if (price <= 0) {
            violations.add("price should be bigger then zero");
        }

        return setToOptional(violations, sender, PRICE_FIELD);
    }

    public Optional<FieldError> isDescriptionValid(String description, Object sender) {
        Set<String> violations = new HashSet<>();
        if (isNull(description)) {
            return Optional.empty();
        }

        if (description.length() > 256) {
            violations.add("description shouldn't be longer then 256 symbols");
        }

        return setToOptional(violations, sender, WEIGHT_FIELD);
    }

    public Optional<FieldError> isWeightValid(Double weight, Object sender) {
        Set<String> violations = new HashSet<>();
        if (isNull(weight)) {
            return Optional.empty();
        }

        if (weight <= 0) {
            violations.add("weight should be bigger then 0");
        }

        return setToOptional(violations, sender, WEIGHT_FIELD);
    }

    public Optional<FieldError> isUpdateNameValid(String name, Object sender) {
        if (isNull(name)) {
            return Optional.empty();
        }
        return isNameValid(name, sender);
    }

    public Optional<FieldError> isUpdatePriceValid(Double price, Object sender) {
        if (isNull(price)) {
            return Optional.empty();
        }
        return isPriceValid(price, sender);
    }

    public Optional<FieldError> isUpdateDescriptionValid(String description, Object sender) {
        if (isNull(description)) {
            return Optional.empty();
        }
        return isDescriptionValid(description, sender);
    }

    public Optional<FieldError> isUpdateWeightValid(Double weight, Object sender) {
        if (isNull(weight)) {
            return Optional.empty();
        }
        return isWeightValid(weight, sender);
    }
}
