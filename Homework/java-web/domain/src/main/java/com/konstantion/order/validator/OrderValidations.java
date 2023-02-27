package com.konstantion.order.validator;

import com.konstantion.product.Product;
import com.konstantion.utils.validator.ValidationsUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
public record OrderValidations() implements ValidationsUtil {
    public Optional<String> isProductsValid(Map<Product, Integer> products) {
        if (isNull(products)) {
            return Optional.of("Products shouldn't be null");
        }

        if (products.isEmpty()) {
            return Optional.of("Products shouldn't be empty");
        }

        return Optional.empty();
    }
}
