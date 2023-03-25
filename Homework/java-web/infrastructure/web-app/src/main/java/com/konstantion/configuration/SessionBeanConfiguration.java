package com.konstantion.configuration;

import com.konstantion.bucket.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class SessionBeanConfiguration {
    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Bucket bucket() {
        return new Bucket();
    }
}
