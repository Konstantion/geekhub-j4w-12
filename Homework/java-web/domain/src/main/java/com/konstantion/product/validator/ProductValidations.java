package com.konstantion.product.validator;


import com.konstantion.utils.validator.ValidationsUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isAlphanumericSpace;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public record ProductValidations() implements ValidationsUtil {
    public Optional<String> isNameValid(String name) {
        List<String> errorList = new ArrayList<>();
        if (isNull(name)) {
            return Optional.of("Product name shouldn't be null");
        }

        if (isBlank(name)) {
            return Optional.of("Product name shouldn't be empty");
        }

        if (name.length() < 3 || name.length() > 32) {
            errorList.add("Product name should be between 3 and 100 characters");
        }

        if (!isAlphanumericSpace(name)) {
            errorList.add("Product name should contain only alphanumeric characters and space");
        }

        return listToOptionalString(errorList);
    }

    public Optional<String> isPriceValid(Double price) {
        List<String> errorList = new ArrayList<>();

        if (isNull(price)) {
            return Optional.of("Product price shouldn't be null");
        }

        if (price < 0) {
            return Optional.of("Product price should be greater or equal to zero");
        }

        return listToOptionalString(errorList);
    }

    public Optional<String> isDescriptionValid(String description) {
        List<String> errorList = new ArrayList<>();
        if (isNull(description)) {
            return Optional.of("Product description shouldn't be null");
        }


        if (description.length() > 255) {
            errorList.add("Product name should be between 3 and 100 characters");
        }

        if (!isAlphanumericSpace(description)) {
            errorList.add("Product name should contain only alphanumeric characters and space");
        }

        return listToOptionalString(errorList);
    }
}
