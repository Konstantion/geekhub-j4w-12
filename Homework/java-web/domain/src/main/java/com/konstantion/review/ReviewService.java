package com.konstantion.review;

import com.konstantion.review.dto.CreationReviewDto;
import com.konstantion.review.dto.ReviewDto;
import com.konstantion.user.User;

import java.util.UUID;

public interface ReviewService {
    ReviewDto createReview(CreationReviewDto creationReviewDto, User user);

    ReviewDto deleteReview(UUID reviewUuid, User user);
}
