package com.konstantion.configuration.domain;

import com.konstantion.bucket.BucketService;
import com.konstantion.bucket.BucketServiceImp;
import com.konstantion.order.OrderRepository;
import com.konstantion.order.OrderService;
import com.konstantion.order.OrderServiceImp;
import com.konstantion.order.validator.OrderValidator;
import com.konstantion.product.ProductRepository;
import com.konstantion.product.ProductService;
import com.konstantion.product.ProductServiceImp;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.reporitories.mappers.ProductRawMapper;
import com.konstantion.reporitories.mappers.ReviewRawMapper;
import com.konstantion.reporitories.mappers.UserRawMapper;
import com.konstantion.review.ReviewRepository;
import com.konstantion.review.ReviewService;
import com.konstantion.review.ReviewServiceImp;
import com.konstantion.review.validator.ReviewValidator;
import com.konstantion.upload.UploadService;
import com.konstantion.user.User;
import com.konstantion.file.MultipartFileValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.UUID;

@Configuration
@ComponentScan("com.konstantion")
public class DomainBeanConfiguration {
    @Bean
    public BucketService bucketService(ProductRepository productRepository) {
        return new BucketServiceImp(productRepository);
    }

    @Bean
    public OrderService orderService(
            OrderRepository orderRepository,
            OrderValidator orderValidator
    ) {
        return new OrderServiceImp(orderRepository, orderValidator);
    }

    @Bean
    public ProductService productService(ProductValidator productValidator,
                                         MultipartFileValidator fileValidator,
                                         ProductRepository productRepository,
                                         UploadService uploadService
    ) {
        return new ProductServiceImp(productValidator, fileValidator, productRepository, uploadService);
    }

    @Bean
    public ProductRawMapper productRawMapper() {
        return new ProductRawMapper();
    }

    @Bean
    public ReviewRawMapper reviewRawMapper() {
        return new ReviewRawMapper();
    }

    @Bean
    public ReviewService reviewService(ReviewValidator reviewValidator,
                                       ReviewRepository reviewRepository) {
        return new ReviewServiceImp(reviewValidator, reviewRepository);
    }

    @Bean
    public User user() {
        return User.builder()
                .email("email")
                .uuid(UUID.fromString("d750e56e-b5e8-11ed-8481-00d8611a4231"))
                .password("password")
                .username("username")
                .build();
    }

    @Bean
    public UserRawMapper userRawMapper() {
        return new UserRawMapper();
    }
}
