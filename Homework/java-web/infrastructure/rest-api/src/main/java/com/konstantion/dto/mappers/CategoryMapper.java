package com.konstantion.dto.mappers;

import com.konstantion.category.Category;
import com.konstantion.category.model.CreationCategoryRequest;
import com.konstantion.category.model.UpdateCategoryRequest;
import com.konstantion.dto.category.CategoryDto;
import com.konstantion.dto.category.CreationCategoryDto;
import com.konstantion.dto.category.UpdateCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category entity);

    List<CategoryDto> toDto(List<Category> entity);

    Category toEntity(CategoryDto dto);

    CreationCategoryRequest toEntity(CreationCategoryDto dto);

    UpdateCategoryRequest toEntity(UpdateCategoryDto dto);
}
