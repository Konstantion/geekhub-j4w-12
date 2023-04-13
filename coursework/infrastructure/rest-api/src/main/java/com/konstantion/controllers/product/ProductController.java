package com.konstantion.controllers.product;

import com.konstantion.controllers.product.converter.ProductMapper;
import com.konstantion.controllers.product.dto.CreateProductRequestDto;
import com.konstantion.controllers.product.dto.ProductDto;
import com.konstantion.product.ProductService;
import com.konstantion.response.ResponseDto;
import com.konstantion.user.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.PRODUCT;
import static com.konstantion.utils.EntityNameConstants.PRODUCTS;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/products")
public record ProductController(
        ProductService productService
) {
    private static final ProductMapper productMapper = ProductMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getProducts(
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
                .message("Product on page")
                .data(Map.of(PRODUCTS, productsDto))
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
                .data(Map.of(PRODUCT, productDto))
                .build();
    }

    @PutMapping("/{id}/activate")
    public ResponseDto activateProductById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        ProductDto productDto = productMapper.toDto(productService.activate(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("Product with id %s activated", id))
                .data(Map.of(PRODUCT, productDto))
                .build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseDto deactivateProductById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        ProductDto productDto = productMapper.toDto(productService.deactivate(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("Product with id %s deactivated", id))
                .data(Map.of(PRODUCT, productDto))
                .build();
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA)
    public ResponseDto createProduct(
            @ModelAttribute CreateProductRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        ProductDto productDto = productMapper.toDto(
                productService.create(
                        productMapper.toCreateProductRequest(requestDto),
                        user
                )
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("Product successfully created")
                .data(Map.of(PRODUCT, productDto))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseDto deleteProduct(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        ProductDto productDto = productMapper.toDto(productService.delete(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message(format("Product with id %s successfully deleted", id))
                .data(Map.of(PRODUCT, productDto))
                .build();
    }
}
