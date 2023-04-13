package com.konstantion.controllers;

import com.konstantion.category.CategoryService;
import com.konstantion.dto.category.CategoryDto;
import com.konstantion.dto.category.CreationCategoryDto;
import com.konstantion.dto.category.UpdateCategoryDto;
import com.konstantion.dto.mappers.CategoryMapper;
import com.konstantion.dto.mappers.OrderMapper;
import com.konstantion.dto.mappers.ProductMapper;
import com.konstantion.dto.mappers.UserMapper;
import com.konstantion.dto.order.OrderDto;
import com.konstantion.dto.product.CreationProductDto;
import com.konstantion.dto.product.ProductDto;
import com.konstantion.dto.product.UpdateProductDto;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.dto.user.UpdateUserRolesDto;
import com.konstantion.order.OrderService;
import com.konstantion.product.Product;
import com.konstantion.product.ProductService;
import com.konstantion.user.User;
import com.konstantion.user.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/admin-api")
public record AdminController(
        CategoryService categoryService,
        OrderService orderService,
        ProductService productService,
        UserService userService
) {
    private static final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;
    private static final OrderMapper orderMapper = OrderMapper.INSTANCE;
    public static final ProductMapper productMapper = ProductMapper.INSTANCE;
    public static final UserMapper userMapper = UserMapper.INSTANCE;

    @PostMapping("/categories")
    public ResponseEntity<ResponseDto> createCategory(
            @RequestBody CreationCategoryDto creationDto,
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

    @PutMapping(path = "/categories/{uuid}")
    public ResponseEntity<ResponseDto> updateCategory(
            @PathVariable("uuid") UUID uuid,
            @RequestBody UpdateCategoryDto updateDto
    ) {
        CategoryDto dto = categoryMapper.toDto(
                categoryService.updateCategory(uuid, categoryMapper.toEntity(updateDto))
        );

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .message(format("Category with id %s updated", dto.uuid()))
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }

    @DeleteMapping("/categories/{uuid}")
    public ResponseEntity<ResponseDto> deleteCategory(
            @PathVariable("uuid") UUID uuid
    ) {
        CategoryDto dto = categoryMapper.toDto(categoryService.deleteCategory(uuid));

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .message(format("Category with id %s deleted", dto.uuid()))
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .build());
    }

    @GetMapping("/orders/all")
    public ResponseEntity<ResponseDto> getAllOrders() {
        List<OrderDto> dto = orderMapper.toDto(orderService.findAll());
        return ResponseEntity.ok(ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("All orders")
                .data(Map.of("orders", dto))
                .timeStamp(now())
                .build());
    }

    @DeleteMapping("/orders/{uuid}")
    public ResponseEntity<ResponseDto> deleteOrderById(
            @PathVariable("uuid") UUID uuid
    ) {
        OrderDto orderDto = orderMapper.toDto(orderService.deleteOrder(uuid));
        return ResponseEntity.ok(ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Order successfully deleted")
                .data(Map.of("order", orderDto))
                .timeStamp(now())
                .build());
    }

    @PutMapping(
            path = "/products/{uuid}",
            consumes = {MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<ResponseDto> updateProduct(
            @PathVariable("uuid") UUID uuid,
            @Parameter(description = "Update product dto")
            @ModelAttribute UpdateProductDto updateDto
    ) {
        ProductDto dto = productMapper.toDto(
                productService.update(uuid, productMapper.toEntity(updateDto))
        );

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .build()
        );
    }

    @PostMapping(path = "/products",
            consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> addProduct(
            @Parameter(description = "Creation product dto")
            @ModelAttribute CreationProductDto product
    ) {
        ProductDto dto = productMapper.toDto(
                productService.create(productMapper.toEntity(product), product.file())
        );
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .statusCode(OK.value())
                        .status(OK)
                        .data(Map.of("uuid", dto.uuid()))
                        .build()
        );
    }

    @DeleteMapping("/products/{uuid}")
    public ResponseEntity<ResponseDto> deleteProduct(
            @PathVariable("uuid") UUID uuid
    ) {
        ProductDto dto = productMapper.toDto(productService.delete(uuid));
        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .message("Product successfully deleted")
                .build()
        );
    }

    @GetMapping("/products/all")
    public ResponseEntity<ResponseDto> getAllProducts(
            @RequestParam("parameter") Optional<String> parameter,
            @RequestParam("pattern") Optional<String> pattern
    ) {
        List<UUID> uuids = productService.getAll(
                        DESC,
                        parameter.orElse("name").toLowerCase(),
                        pattern.orElse("").toLowerCase()).stream()
                .map(Product::uuid).toList();


        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .message("Products")
                .data(Map.of("uuids", uuids))
                .build()
        );
    }

    @PutMapping("/users/{uuid}/enable")
    public ResponseEntity<ResponseDto> enableUser(
            @PathVariable("uuid") UUID userId
    ) {
        UUID uuid = userService.enableUser(userId);

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .message("User enabled")
                .status(OK)
                .data(Map.of("uuid", uuid))
                .build()
        );
    }

    @PutMapping("/users/{uuid}/disable")
    public ResponseEntity<ResponseDto> disableUser(
            @PathVariable("uuid") UUID userId
    ) {
        UUID uuid = userService.disableUser(userId);

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .message("User disabled")
                .status(OK)
                .data(Map.of("uuid", uuid))
                .build()
        );
    }

    @PutMapping("/users/{uuid}/roles")
    public ResponseEntity<ResponseDto> updateUserRoles(
            @PathVariable("uuid") UUID userId,
            @RequestBody UpdateUserRolesDto updateUserRolesDto
    ) {
        User user = userService.editUserRoles(
                userId,
                userMapper.toEntity(updateUserRolesDto)
        );

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .message("User roles updated")
                .status(OK)
                .data(Map.of("user", user))
                .build()
        );
    }
}
