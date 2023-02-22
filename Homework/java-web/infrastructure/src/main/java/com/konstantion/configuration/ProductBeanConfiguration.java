package com.konstantion.configuration;

import com.konstantion.product.CliProductService;
import com.konstantion.product.ProductRepository;
import com.konstantion.product.ProductService;
import com.konstantion.product.validator.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.konstantion")
public class ProductBeanConfiguration {

    @Autowired
    @Bean
    ProductService productService(ProductValidator productValidator,
                                  ProductRepository productRepository
    ) {
        return new CliProductService(productValidator, productRepository);
    }
}
