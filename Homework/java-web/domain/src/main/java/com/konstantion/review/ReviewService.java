package com.konstantion.review;

import com.konstantion.review.model.CreationReviewRequest;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    Review createReview(CreationReviewRequest creationReviewDto, User user);

    Review deleteReview(UUID reviewUuid, User user);

    Review getReviewById(UUID uuid);

    List<Review> getProductReviews(UUID productUuid);

    List<Review> getUserReviews(UUID userUuid, User user);

    Double getProductRating(UUID productUuid);
}
