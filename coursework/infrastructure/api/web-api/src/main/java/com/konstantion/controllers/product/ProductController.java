package com.konstantion.controllers.product;

import com.konstantion.dto.product.converter.ProductMapper;
import com.konstantion.dto.product.dto.ProductDto;
import com.konstantion.product.ProductService;
import com.konstantion.response.ResponseDto;
import com.konstantion.utils.HashMaps;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.PRODUCT;
import static com.konstantion.utils.EntityNameConstants.PRODUCTS;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/products")
public record ProductController(
        ProductService productService
) {
    private static final ProductMapper productMapper = ProductMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllActiveProducts(
            @RequestParam("page") Optional<Integer> pageNumber,
            @RequestParam("size") Optional<Integer> pageSize,
            @RequestParam("orderBy") Optional<String> fieldName,
            @RequestParam("pattern") Optional<String> searchPattern,
            @RequestParam("categoryUuid") Optional<UUID> categoryUuid,
            @RequestParam("ascending") Optional<Boolean> ascending
    ) {
        Page<ProductDto> productsDto = productMapper.toDto(
                productService.getAll(
                        productMapper.toGetProductsRequest(
                                pageNumber.orElse(1),
                                pageSize.orElse(6),
                                fieldName.orElse("name"),
                                searchPattern.orElse(""),
                                categoryUuid.orElse(null),
                                ascending.orElse(true)
                        )
                )
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("Active page products successfully returned")
                .data(HashMaps.of(PRODUCTS, productsDto))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseDto getProductById(
            @PathVariable("id") UUID id
    ) {
        ProductDto productDto = productMapper.toDto(productService.getById(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("Product with id %s", id))
                .data(HashMaps.of(PRODUCT, productDto))
                .build();
    }
}
