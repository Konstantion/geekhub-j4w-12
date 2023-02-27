package com.konstantion.review;

import com.konstantion.review.dto.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    ReviewDto toDto(Review entity);

    List<ReviewDto> toDto(List<Review> entity);
}
