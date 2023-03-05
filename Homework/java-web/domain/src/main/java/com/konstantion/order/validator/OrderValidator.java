package com.konstantion.order.validator;

import com.konstantion.order.Order;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.*;

@Component
public record OrderValidator(OrderValidations orderValidations) {

    public ValidationResult isOrderValid(Order order) {
        Set<FieldError> validationErrors = new HashSet<>();


        orderValidations.isProductsValid(order.products()).ifPresent(
                s -> validationErrors.add(new FieldError(
                        order.toString(), "Products", s)
                )
        );

        if (validationErrors.isEmpty()) {
            return ValidationResult.empty();
        }

        return ValidationResult.of(validationErrors);
    }
}
