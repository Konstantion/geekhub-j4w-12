package com.konstantion.review;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.product.ProductService;
import com.konstantion.product.ProductServiceImp;
import com.konstantion.review.dto.CreationReviewDto;
import com.konstantion.review.dto.ReviewDto;
import com.konstantion.review.validator.ReviewValidator;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

public record ReviewServiceImp(ReviewValidator reviewValidator,
                               ReviewRepository reviewRepository,
                               ProductService productService)
        implements ReviewService {
    static ReviewMapper reviewMapper = ReviewMapper.INSTANCE;
    static Logger logger = LoggerFactory.getLogger(ReviewServiceImp.class);

    @Override
    public ReviewDto createReview(CreationReviewDto creationReviewDto, User user) {
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
        ReviewDto reviewDto = reviewMapper.toDto(review);

        logger.info("{} successfully created", reviewDto);

        return reviewDto;
    }

    @Override
    public ReviewDto deleteReview(UUID reviewUuid, User user) {
        Review review = reviewRepository.findById(reviewUuid).orElseThrow(() ->
                new BadRequestException(format("Review with uuid %s doesn't exist", reviewUuid)
                ));

        reviewRepository.deleteById(reviewUuid);

        logger.info("Product with uuid {} successfully delete", reviewUuid);
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewDto getReviewById(UUID uuid) {
        Review review = reviewRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Review with uuid %s doesn't exist", uuid)
                ));

        return reviewMapper.toDto(review);
    }

    @Override
    public List<ReviewDto> getProductReviews(UUID productUuid) {
        productService.getById(productUuid);
        List<Review> reviews = reviewRepository.findByProductId(productUuid);

        return reviewMapper.toDto(reviews);
    }

    @Override
    public List<ReviewDto> getUserReviews(UUID userUuid, User user) {
        List<Review> reviews = reviewRepository.findByUserId(userUuid);

        return reviewMapper.toDto(reviews);
    }

    @Override
    public Double getProductRating(UUID productUuid) {
        productService.getById(productUuid);
        return reviewRepository.findProductRating(productUuid);
    }
}
