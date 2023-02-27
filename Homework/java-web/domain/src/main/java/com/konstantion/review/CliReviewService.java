package com.konstantion.review;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.review.dto.CreationReviewDto;
import com.konstantion.review.dto.ReviewDto;
import com.konstantion.review.validator.ReviewValidator;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.String.format;

public record CliReviewService(ReviewValidator reviewValidator,
                               ReviewRepository reviewRepository)
        implements ReviewService {
    static ReviewMapper MAPPER = ReviewMapper.INSTANCE;
    static Logger logger = LoggerFactory.getLogger(CliReviewService.class);

    @Override
    public ReviewDto createReview(CreationReviewDto creationReviewDto, User user) {
        ValidationResult validationResult = reviewValidator.validate(creationReviewDto);
        if (validationResult.errorsPresent()) {
            throw new ValidationException("Failed to create review, given data is invalid",
                    validationResult.getErrorsAsMap());
        }

        Review review = Review.builder()
                .uuid(UUID.randomUUID())
                .message(creationReviewDto.message())
                .rating(creationReviewDto.rating())
                .userUuid(user.uuid())
                .productUuid(creationReviewDto.productUuid())
                .createdAt(LocalDateTime.now())
                .build();

        review = reviewRepository.save(review);
        ReviewDto reviewDto = MAPPER.toDto(review);

        logger.info("{} successfully created", reviewDto);

        return reviewDto;
    }

    @Override
    public ReviewDto deleteReview(UUID reviewUuid, User user) {
        Review review = reviewRepository.findByUuid(reviewUuid).orElseThrow(() ->
                new BadRequestException(format("Review with uuid %s doesn't exist", reviewUuid)
                ));

        reviewRepository.deleteByUuid(reviewUuid);

        logger.info("Product with uuid {} successfully delete", reviewUuid);
        return MAPPER.toDto(review);
    }
}
