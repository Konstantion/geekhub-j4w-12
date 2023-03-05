package com.konstantion.file;

import com.konstantion.utils.validator.ValidationsUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
public record MultipartFileValidations() implements ValidationsUtil {
    private static final Double BYTES_IN_MEGABYTE = 1e6;

    Optional<String> isFileValid(MultipartFile file) {
        List<String> errorList = new ArrayList<>();
        if (isNull(file) || file.getOriginalFilename().isEmpty()) {
            return Optional.of("File shouldn't be empty");
        }

        if (file.getSize() > 10 * BYTES_IN_MEGABYTE) {
            errorList.add("File size should be less than 10 MB");
        }

        if (!file.getContentType().contains("image")) {
            errorList.add("File should be of type image");
        }

        return listToOptionalString(errorList);
    }
}
