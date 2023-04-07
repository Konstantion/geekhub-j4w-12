package com.konstantion.configuration;

import com.konstantion.bucket.BucketService;
import com.konstantion.bucket.BucketServiceImpl;
import com.konstantion.category.CategoryRepository;
import com.konstantion.category.CategoryService;
import com.konstantion.category.CategoryServiceImpl;
import com.konstantion.category.validator.CategoryValidator;
import com.konstantion.email.EmailService;
import com.konstantion.file.MultipartFileValidator;
import com.konstantion.order.OrderRepository;
import com.konstantion.order.OrderService;
import com.konstantion.order.OrderServiceImpl;
import com.konstantion.order.validator.OrderValidator;
import com.konstantion.product.ProductRepository;
import com.konstantion.product.ProductService;
import com.konstantion.product.ProductServiceImpl;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.ragistration.token.ConfirmationTokenService;
import com.konstantion.review.ReviewRepository;
import com.konstantion.review.ReviewService;
import com.konstantion.review.ReviewServiceImpl;
import com.konstantion.review.validator.ReviewValidator;
import com.konstantion.upload.UploadService;
import com.konstantion.user.*;
import com.konstantion.user.validator.UserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.util.Set;
import java.util.UUID;

@Configuration
@ComponentScan("com.konstantion")
public class DomainBeanConfiguration {
    @Bean
    public BucketService bucketService(ProductRepository productRepository, ProductService productService) {
        return new BucketServiceImpl(productRepository, productService);
    }

    @Bean
    public OrderService orderService(
            OrderRepository orderRepository,
            OrderValidator orderValidator,
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        return new OrderServiceImpl(orderRepository, orderValidator, userRepository, productRepository);
    }

    @Bean
    public ProductService productService(ProductValidator productValidator,
                                         MultipartFileValidator fileValidator,
                                         ProductRepository productRepository,
                                         UploadService uploadService,
                                         CategoryService categoryService
    ) {
        return new ProductServiceImpl(productValidator, fileValidator, productRepository, uploadService, categoryService);
    }

    @Bean
    public ReviewService reviewService(ReviewValidator reviewValidator,
                                       ReviewRepository reviewRepository,
                                       ProductService productService) {
        return new ReviewServiceImpl(reviewValidator, reviewRepository, productService);
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
    public UploadService uploadService(@Value("${upload.name}") String uploadName) {
        return new UploadService(new File(uploadName).getAbsolutePath());
    }

    @Bean
    public CategoryService categoryService(
            CategoryRepository categoryRepository,
            CategoryValidator categoryValidator) {
        return new CategoryServiceImpl(categoryRepository, categoryValidator);
    }

    @Bean
    public UserService userService(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            ConfirmationTokenService tokenService,
            UserValidator userValidator,
            EmailService emailService
    ) {
        return new UserServiceImpl(userRepository, passwordEncoder, tokenService, userValidator, emailService);
    }
}
