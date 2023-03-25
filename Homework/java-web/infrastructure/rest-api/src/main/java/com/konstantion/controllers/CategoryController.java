package com.konstantion.controllers;

import com.konstantion.category.CategoryService;
import com.konstantion.category.dto.CategoryDto;
import com.konstantion.category.dto.CreationCategoryDto;
import com.konstantion.category.dto.UpdateCategoryDto;
import com.konstantion.response.Response;
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
    @GetMapping()
    public ResponseEntity<Response> getCategories() {
        List<UUID> categoriesUuid = categoryService.getCategories().stream()
                .map(CategoryDto::uuid).toList();

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .message("All categories ids")
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuids", categoriesUuid))
                .build());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Response> getCategory(
            @PathVariable("uuid") UUID uuid
    ) {
        CategoryDto dto = categoryService.getCategoryById(uuid);

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .message(format("Category with id %s", uuid))
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("category", dto))
                .build());
    }

    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Response> createCategory(
            @ModelAttribute CreationCategoryDto creationDto,
            @AuthenticationPrincipal User user
            ) {
        CategoryDto dto = categoryService.createCategory(creationDto, user);

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .message(format("Category created with id %s", dto.uuid()))
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }

    @PutMapping(path = "/{uuid}", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Response> updateCategory(
            @PathVariable("uuid") UUID uuid,
            @ModelAttribute UpdateCategoryDto updateDto,
            @AuthenticationPrincipal User user
            ) {
        CategoryDto dto = categoryService.updateCategory(uuid, updateDto, user);

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .message(format("Category with id %s updated", dto.uuid()))
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Response> deleteCategory(
            @PathVariable("uuid") UUID uuid,
            @AuthenticationPrincipal User user
    ) {
        CategoryDto dto = categoryService.deleteCategory(uuid, user);

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .message(format("Category with id %s deleted", dto.uuid()))
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }
}
