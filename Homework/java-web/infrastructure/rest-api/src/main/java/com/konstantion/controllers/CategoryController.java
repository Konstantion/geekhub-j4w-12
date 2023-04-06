package com.konstantion.controllers;

import com.konstantion.category.Category;
import com.konstantion.category.CategoryService;
import com.konstantion.dto.category.CategoryDto;
import com.konstantion.dto.category.CreationCategoryDto;
import com.konstantion.dto.category.UpdateCategoryDto;
import com.konstantion.dto.mappers.CategoryMapper;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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

    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto> createCategory(
            @ModelAttribute CreationCategoryDto creationDto,
            @AuthenticationPrincipal User user
    ) {
        CategoryDto dto = categoryMapper.toDto(
                categoryService.createCategory(categoryMapper.toEntity(creationDto), user)
        );

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .message(format("Category created with id %s", dto.uuid()))
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }

    @PutMapping(path = "/{uuid}", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto> updateCategory(
            @PathVariable("uuid") UUID uuid,
            @ModelAttribute UpdateCategoryDto updateDto,
            @AuthenticationPrincipal User user
    ) {
        CategoryDto dto = categoryMapper.toDto(
                categoryService.updateCategory(uuid, categoryMapper.toEntity(updateDto), user)
        );

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .message(format("Category with id %s updated", dto.uuid()))
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ResponseDto> deleteCategory(
            @PathVariable("uuid") UUID uuid,
            @AuthenticationPrincipal User user
    ) {
        CategoryDto dto = categoryMapper.toDto(categoryService.deleteCategory(uuid, user));

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .message(format("Category with id %s deleted", dto.uuid()))
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }
}
