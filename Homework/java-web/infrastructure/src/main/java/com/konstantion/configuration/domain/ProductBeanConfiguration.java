package com.konstantion.configuration.domain;

import com.konstantion.product.CliProductService;
import com.konstantion.product.ProductRawMapper;
import com.konstantion.product.ProductRepository;
import com.konstantion.product.ProductService;
import com.konstantion.product.validator.ProductValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.konstantion")
public class ProductBeanConfiguration {
    @Bean
    public ProductService productService(ProductValidator productValidator,
                                  ProductRepository productRepository
    ) {
        return new CliProductService(productValidator, productRepository);
    }

    @Bean
    public ProductRawMapper productRawMapper() {
        return new ProductRawMapper();
    }
}
