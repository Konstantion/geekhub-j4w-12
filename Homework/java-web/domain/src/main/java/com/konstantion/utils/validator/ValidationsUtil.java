package com.konstantion.utils.validator;

import java.util.List;
import java.util.Optional;

public interface ValidationsUtil {
    default Optional<String> listToOptionalString(List<String> errorList) {
        String data = String.join(
                System.lineSeparator(),
                errorList
        );

        if (data.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(data);
    }
}
