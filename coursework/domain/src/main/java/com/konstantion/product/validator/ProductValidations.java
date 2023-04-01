package com.konstantion.product.validator;

import com.konstantion.utils.validator.ValidationUtil;
import org.springframework.stereotype.Component;

@Component
public record ProductValidations(

) implements ValidationUtil {
}
