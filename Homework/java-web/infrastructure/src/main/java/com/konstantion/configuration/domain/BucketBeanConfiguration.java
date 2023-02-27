package com.konstantion.configuration.domain;

import com.konstantion.bucket.BucketService;
import com.konstantion.bucket.CliBucketService;
import com.konstantion.product.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.konstantion")
public class BucketBeanConfiguration {
    @Bean
    public BucketService bucketService(ProductRepository productRepository) {
        return new CliBucketService(productRepository);
    }
}
