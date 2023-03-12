package com.konstantion.controllers;


import com.konstantion.exceptions.FileIOException;
import com.konstantion.product.ProductService;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.response.Response;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.time.LocalDateTime.now;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/web-api/products")
public record ProductController(ProductService productService) {
    @GetMapping()
    public ResponseEntity<Response> getProducts(
            @RequestParam("parameter") Optional<String> parameter,
            @RequestParam("pattern") Optional<String> pattern
    ) {

        if (pattern.orElse("").equals("error")) {
            throw new FileIOException("What the hell just happened");
        }

        List<UUID> uuids = productService.getAll(
                        DESC,
                        parameter.orElse("name").toLowerCase(),
                        pattern.orElse("").toLowerCase()).stream()
                .map(ProductDto::uuid).toList();


        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuids", uuids))
                .build()
        );
    }

    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Response> addProduct(
            @Parameter(description = "Creation product dto")
            @ModelAttribute CreationProductDto productCreationDto
    ) {
        ProductDto dto = productService.create(productCreationDto, productCreationDto.file());
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(OK.value())
                        .status(OK)
                        .data(Map.of("uuid", dto.uuid()))
                        .build()
        );
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Response> deleteProduct(
            @PathVariable("uuid") UUID uuid
    ) {
        ProductDto dto = productService.delete(uuid);
        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .message("Product successfully deleted")
                .build()
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Response> getProduct(
            @PathVariable("uuid") UUID uuid
    ) {
        ProductDto dto = productService.getById(uuid);
        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("product", dto))
                .build()
        );
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Response> updateProduct(
            @PathVariable("uuid") UUID uuid,
            @ModelAttribute ProductDto updateDto
    ) {
        ProductDto dto = productService.update(uuid, updateDto);

        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("uuid", dto.uuid()))
                .build()
        );
    }

    @GetMapping("/{uuid}/image/encoded")
    public ResponseEntity<Response> getProductImageEncoded(
            @PathVariable("uuid") UUID uuid
    ) {
        String base64Encoded = productService.getProductImage(uuid);
        return ResponseEntity.ok(Response.builder()
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
        return productService.getById(uuid).imageBytes();
    }
}
