package com.konstantion.upload;

import com.konstantion.exceptions.FileIOException;
import com.konstantion.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.join;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

public record UploadService(String uploadPath) {
    private static final String PATH_SEPARATOR = System.getProperty("file.separator");

    public String uploadProductImage(MultipartFile file) {
        if (isNull(file) || isBlank(file.getOriginalFilename())) {
            throw new ValidationException("File is not valid", Map.of("file", "File shouldn't be empty"));
        }

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();

        try {
            file.transferTo(new File(
                    join(
                            PATH_SEPARATOR,
                            uploadPath,
                            resultFilename
                    )
            ));
        } catch (IOException e) {
            throw new FileIOException(e.getMessage());
        }

        return resultFilename;
    }
}
