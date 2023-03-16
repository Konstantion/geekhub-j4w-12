package com.konstantion.configuration;

import com.konstantion.bucket.BucketService;
import com.konstantion.bucket.BucketServiceImp;
import com.konstantion.category.CategoryRepository;
import com.konstantion.category.CategoryService;
import com.konstantion.category.CategoryServiceImp;
import com.konstantion.category.validator.CategoryValidator;
import com.konstantion.file.MultipartFileValidator;
import com.konstantion.order.OrderRepository;
import com.konstantion.order.OrderService;
import com.konstantion.order.OrderServiceImp;
import com.konstantion.order.validator.OrderValidator;
import com.konstantion.product.ProductRepository;
import com.konstantion.product.ProductService;
import com.konstantion.product.ProductServiceImp;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.reporitories.JdbcCategoryRepository;
import com.konstantion.reporitories.mappers.UserRawMapper;
import com.konstantion.review.ReviewRepository;
import com.konstantion.review.ReviewService;
import com.konstantion.review.ReviewServiceImp;
import com.konstantion.review.validator.ReviewValidator;
import com.konstantion.upload.UploadService;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import com.konstantion.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Set;
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
            OrderValidator orderValidator,
            UserRepository userRepository
    ) {
        return new OrderServiceImp(orderRepository, orderValidator, userRepository);
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
    public ReviewService reviewService(ReviewValidator reviewValidator,
                                       ReviewRepository reviewRepository) {
        return new ReviewServiceImp(reviewValidator, reviewRepository);
    }

    @Bean
    public User user() {
        return User.builder()
                .email("email")
                .id(UUID.fromString("d750e56e-b5e8-11ed-8481-00d8611a4231"))
                .password("password")
                .firstName("first")
                .lastName("last")
                .phoneNumber("0500962023")
                .roles(Set.of(Role.ADMIN))
                .enabled(true)
                .accountNonLocked(true)
                .build();
    }

    @Bean
    public UserRawMapper userRawMapper() {
        return new UserRawMapper();
    }

    @Bean
    public UploadService uploadService(@Value("${upload.name}") String uploadName) {
        return new UploadService(new File(uploadName).getAbsolutePath());
    }

    @Bean
    public CategoryService categoryService(
            CategoryRepository categoryRepository,
            CategoryValidator categoryValidator) {
        return new CategoryServiceImp(categoryRepository, categoryValidator);
    }
}
