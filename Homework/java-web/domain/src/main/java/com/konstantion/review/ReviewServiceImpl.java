package com.konstantion.review;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ForbiddenException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.product.ProductService;
import com.konstantion.review.model.CreationReviewRequest;
import com.konstantion.review.validator.ReviewValidator;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.konstantion.user.Role.ADMIN;
import static com.konstantion.user.Role.MODERATOR;
import static java.lang.String.format;

public record ReviewServiceImpl(ReviewValidator reviewValidator,
                                ReviewRepository reviewRepository,
                                ProductService productService)
        implements ReviewService {
    private static Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Override
    public Review createReview(CreationReviewRequest creationReviewDto, User user) {
        ValidationResult validationResult = reviewValidator.validate(creationReviewDto);
        if (validationResult.errorsPresent()) {
            throw new ValidationException("Failed to create review, given data is invalid",
                    validationResult.getErrorsAsMap());
        }

        Review review = Review.builder()
                .message(creationReviewDto.message())
                .rating(creationReviewDto.rating())
                .userUuid(user.getId())
                .productUuid(creationReviewDto.productUuid())
                .createdAt(LocalDateTime.now())
                .build();

        review = reviewRepository.save(review);

        logger.info("Review with id {} successfully created", review.uuid());

        return review;
    }

    @Override
    public Review deleteReview(UUID reviewUuid, User user) {
        Review review = getReviewById(reviewUuid);

        if (!review.userUuid().equals(user.getId())
            && !user.hasRole(ADMIN)
            && !user.hasRole(MODERATOR)) {
            throw new ForbiddenException("Not enough authorities to delete review");
        }

        reviewRepository.deleteById(reviewUuid);

        logger.info("Review with uuid {} successfully delete", reviewUuid);
        return review;
    }

    @Override
    public Review getReviewById(UUID uuid) {
        return reviewRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Review with uuid %s doesn't exist", uuid)
                ));
    }

    @Override
    public List<Review> getProductReviews(UUID productUuid) {
        productService.getById(productUuid);
        return reviewRepository.findByProductId(productUuid);
    }

    @Override
    public List<Review> getUserReviews(UUID userUuid, User user) {
        if (!user.getId().equals(userUuid)
            && !user.hasRole(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to get user reviews");
        }
        return reviewRepository.findByUserId(userUuid);
    }

    @Override
    public Double getProductRating(UUID productUuid) {
        productService.getById(productUuid);
        return reviewRepository.findProductRating(productUuid);
    }
}
