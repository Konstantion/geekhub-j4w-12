package com.konstantion.guest.validator;

import com.konstantion.utils.validator.ValidationUtil;
import org.springframework.stereotype.Component;

@Component
public record GuestValidation(

) implements ValidationUtil {
}
