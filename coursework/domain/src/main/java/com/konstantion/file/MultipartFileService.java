package com.konstantion.file;

import org.springframework.web.multipart.MultipartFile;

public interface MultipartFileService {
    byte[] getFileBytes(MultipartFile file);
}
