package com.konstantion.controllers.category.converter;

import com.konstantion.category.Category;
import com.konstantion.category.model.CreateCategoryRequest;
import com.konstantion.controllers.category.dto.CategoryDto;
import com.konstantion.controllers.category.dto.CreateCategoryRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category entity);

    List<CategoryDto> toDto(List<Category> entities);

    CreateCategoryRequest toCreateCategoryRequest(CreateCategoryRequestDto dto);
}
