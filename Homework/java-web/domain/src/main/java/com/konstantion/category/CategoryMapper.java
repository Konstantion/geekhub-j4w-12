package com.konstantion.category;

import com.konstantion.category.dto.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category entity);

    List<CategoryDto> toDto(List<Category> entity);

    Category toEntity(CategoryDto dto);
}
