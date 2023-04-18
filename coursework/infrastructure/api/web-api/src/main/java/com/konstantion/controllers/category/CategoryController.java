package com.konstantion.controllers.category;

import com.konstantion.category.CategoryService;
import com.konstantion.dto.category.converter.CategoryMapper;
import com.konstantion.dto.category.dto.CategoryDto;
import com.konstantion.response.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.CATEGORIES;
import static com.konstantion.utils.EntityNameConstants.CATEGORY;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/categories")
public record CategoryController(
        CategoryService categoryService
) {
    private static final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllCategories() {
        List<CategoryDto> dtos = categoryMapper.toDto(categoryService.getAll());

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("All categories")
                .data(Map.of(CATEGORIES, dtos))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseDto getCategoryById(
            @PathVariable("id") UUID id
    ) {
        CategoryDto dto = categoryMapper.toDto(categoryService.getById(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("Category with id %s", dto.id()))
                .data(Map.of(CATEGORY, dto))
                .build();
    }
}
