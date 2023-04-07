package com.konstantion.controllers;


import com.konstantion.dto.mappers.ProductMapper;
import com.konstantion.dto.product.CreationProductDto;
import com.konstantion.dto.product.ProductDto;
import com.konstantion.dto.product.UpdateProductDto;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.product.Product;
import com.konstantion.product.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/web-api/products")
@SecurityRequirement(name = "bearerAuth")
public record ProductController(ProductService productService) {
    public static final ProductMapper productMapper = ProductMapper.INSTANCE;

    @GetMapping()
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> getProductsPage(
            @RequestParam("page") Optional<Integer> pageNumber,
            @RequestParam("size") Optional<Integer> pageSize,
            @RequestParam("orderBy") Optional<String> fieldName,
            @RequestParam("pattern") Optional<String> searchPattern,
            @RequestParam("categoryUuid") Optional<UUID> categoryUuid,
            @RequestParam("ascending") Optional<Boolean> ascending
    ) {
        Page<ProductDto> page = productMapper.toDto(productService.getAll(
                pageNumber.orElse(1),
                pageSize.orElse(4),
                fieldName.orElse("name"),
                searchPattern.orElse(""),
                categoryUuid.orElse(null),
                ascending.orElse(true)
        ));


        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("page", page))
                .build()
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto> getProduct(
            @PathVariable("uuid") UUID uuid
    ) {
        ProductDto dto = productMapper.toDto(productService.getById(uuid));
        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("product", dto))
                .build()
        );
    }

    @PutMapping(
            path = "/{uuid}",
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

    @GetMapping("/{uuid}/image/encoded")
    public ResponseEntity<ResponseDto> getProductImageEncoded(
            @PathVariable("uuid") UUID uuid
    ) {
        String base64Encoded = productService.getProductImageEncoded(uuid);
        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("imageUrl", base64Encoded))
                .build()
        );
    }

    @GetMapping(value = "/{uuid}/image", produces = {IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE})
    public byte[] getProductImage(
            @PathVariable("uuid") UUID uuid
    ) {
        return productService.getProductImageBytes(uuid);
    }
}
