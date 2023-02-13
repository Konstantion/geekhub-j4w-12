package product.validator;

import product.dto.CreationProductDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductValidator {
    private final ProductValidations productValidations;

    public ProductValidator(ProductValidations creationProductDtoValidator) {
        this.productValidations = creationProductDtoValidator;
    }

    public Optional<Map<String, String>> validate(CreationProductDto creationProductDto) {
        Map<String, String> validationErrorsMap = new HashMap<>();
        productValidations.isNameValid(creationProductDto.getName()).ifPresent(
                s -> validationErrorsMap.put("Name", s)
        );

        productValidations.isPriceValid(creationProductDto.getPrice()).ifPresent(
                s -> validationErrorsMap.put("Price", s)
        );

        if (validationErrorsMap.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(validationErrorsMap);
    }
}
