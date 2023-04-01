package com.konstantion.user.validator;

import com.konstantion.utils.validator.ValidationUtil;
import org.springframework.stereotype.Component;

@Component
public record UserValidations(

) implements ValidationUtil {
}
