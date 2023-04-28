package com.konstantion.file.validator;

import com.konstantion.utils.validator.ValidationUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static java.util.Objects.isNull;

@Component
public record MultipartFileValidations() implements ValidationUtil {
    private static final Double BYTES_IN_MEGABYTE = 1e6;
    private static final String FILE = "file";
    Optional<FieldError> isFileValid(MultipartFile file) {
        Set<String> errorSet = new HashSet<>();
        if (isNull(file) || file.getOriginalFilename().isEmpty()) {
            errorSet.add("File shouldn't be empty");
            return setToOptional(errorSet, file, FILE);
        }

        if (file.getSize() > 10 * BYTES_IN_MEGABYTE) {
            errorSet.add("File size should be less than 10 MB");
        }

        if (!file.getContentType().contains("image")) {
            errorSet.add("File should be of type image");
        }

        return setToOptional(errorSet, file, FILE);
    }
}
