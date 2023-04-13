package com.konstantion.controllers;

import com.konstantion.category.Category;
import com.konstantion.category.CategoryService;
import com.konstantion.dto.category.CategoryDto;
import com.konstantion.dto.mappers.CategoryMapper;
import com.konstantion.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public ResponseEntity<ResponseDto> getCategories() {
        List<UUID> categoriesUuid = categoryService.getCategories().stream()
                .map(Category::uuid).toList();

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .message("All categories ids")
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuids", categoriesUuid))
                .build());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto> getCategory(
            @PathVariable("uuid") UUID uuid
    ) {
        CategoryDto dto = categoryMapper.toDto(categoryService.getCategoryById(uuid));

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .message(format("Category with id %s", uuid))
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("category", dto))
                .build());
    }
}
