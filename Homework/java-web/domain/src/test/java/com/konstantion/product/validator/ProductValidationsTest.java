package com.konstantion.product.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProductValidationsTest {
    private ProductValidations productValidations = new ProductValidations();

    @Test
    void process_returnEmptyOptional_whenIsNameValid_withValidName() {
        String name = "Bread";

        Optional<String> validationError = productValidations.isNameValid(name);

        assertThat(validationError).isEmpty();
    }

    @ParameterizedTest
    @CsvSource(
            value = {"null,Product name shouldn't be null",
                    "'',Product name shouldn't be empty",
                    "Na,Product name should be between 3 and 100 characters",
                    "Na_#me,Product name should contain only alphanumeric characters and space"},
            nullValues = {"null"}
    )
    void process_returnOptionalWithMessage_whenIsNameValid_withInvalidName(String name,
                                                                           String expected) {
        Optional<String> validationError = productValidations.isNameValid(name);

        assertThat(validationError)
                .isPresent()
                .contains(expected);
    }

    @Test
    void process_returnEmptyOptional_whenIsPriceValid_withValidPrice() {
        Double price = 10.0;

        Optional<String> validationError = productValidations.isPriceValid(price);

        assertThat(validationError).isEmpty();
    }

    @ParameterizedTest
    @CsvSource(
            value = {"null,Product price shouldn't be null",
                    "-1.0,Product price should be greater or equal to zero"},
            nullValues = {"null"}
    )
    void process_returnOptionalWithMessage_whenIsPriceValid_withInvalidPrice(Double price,
                                                                             String expected) {
        Optional<String> validationError = productValidations.isPriceValid(price);

        assertThat(validationError)
                .isPresent()
                .contains(expected);
    }
}