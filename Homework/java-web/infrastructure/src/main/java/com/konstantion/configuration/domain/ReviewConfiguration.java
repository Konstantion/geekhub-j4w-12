package com.konstantion.configuration.domain;

import com.konstantion.review.CliReviewService;
import com.konstantion.reporitories.mappers.ReviewRawMapper;
import com.konstantion.review.ReviewRepository;
import com.konstantion.review.ReviewService;
import com.konstantion.review.validator.ReviewValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.konstantion")
public class ReviewConfiguration {
    @Bean
    public ReviewRawMapper rawMapper() {
        return new ReviewRawMapper();
    }

    @Bean
    public ReviewService reviewService(ReviewValidator reviewValidator,
                                       ReviewRepository reviewRepository) {
        return new CliReviewService(reviewValidator, reviewRepository);
    }
}
