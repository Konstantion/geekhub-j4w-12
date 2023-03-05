package com.konstantion.configuration;

import com.konstantion.upload.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.File;

@Configuration
@PropertySource("classpath:application.yml")
public class BeenConfiguration {
    @Bean
    public UploadService uploadService(@Value("${upload.name}") String uploadName) {
        return new UploadService(new File(uploadName).getAbsolutePath());
    }
}
