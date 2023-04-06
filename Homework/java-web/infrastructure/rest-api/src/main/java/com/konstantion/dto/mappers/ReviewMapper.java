package com.konstantion.dto.mappers;

import com.konstantion.dto.review.CreationReviewDto;
import com.konstantion.dto.review.ReviewDto;
import com.konstantion.review.Review;
import com.konstantion.review.model.CreationReviewRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    ReviewDto toDto(Review entity);

    List<ReviewDto> toDto(List<Review> entity);

    CreationReviewRequest toEntity(CreationReviewDto creationReviewDto);
}
