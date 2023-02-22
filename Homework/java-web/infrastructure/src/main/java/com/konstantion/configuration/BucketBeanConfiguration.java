package com.konstantion.configuration;

import com.konstantion.bucket.BucketService;
import com.konstantion.bucket.CliBucketService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.konstantion")
public class BucketBeanConfiguration {
    public BucketService bucketService() {
        return new CliBucketService();
    }
}
