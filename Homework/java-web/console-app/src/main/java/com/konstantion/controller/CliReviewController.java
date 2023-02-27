package com.konstantion.controller;

import com.konstantion.review.ReviewService;
import com.konstantion.review.dto.CreationReviewDto;
import com.konstantion.review.dto.ReviewDto;
import com.konstantion.user.User;
import org.springframework.stereotype.Controller;

@Controller
public record CliReviewController(ReviewService reviewService, User user) {
        public ReviewDto createReview(CreationReviewDto creationReviewDto) {
            return reviewService.createReview(creationReviewDto, user);
        }
}
