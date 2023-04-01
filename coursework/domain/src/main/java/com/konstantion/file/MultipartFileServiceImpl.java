package com.konstantion.file;

import com.konstantion.exception.FileIOException;
import com.konstantion.file.validator.MultipartFileValidator;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public record MultipartFileServiceImpl(
        MultipartFileValidator fileValidator
) implements MultipartFileService {
    public byte[] getFileBytes(MultipartFile file) {
        ValidationResult validationResult = fileValidator.validate(file);
        validationResult.validOrTrow();
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new FileIOException(e.getMessage());
        }
    }
}
