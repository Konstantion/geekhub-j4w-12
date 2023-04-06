package com.konstantion.controllers;

import com.konstantion.bucket.Bucket;
import com.konstantion.bucket.BucketService;
import com.konstantion.dto.mappers.ProductMapper;
import com.konstantion.dto.product.ProductDto;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.product.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNullElse;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/buckets")
public record BucketController(BucketService bucketService, Bucket bucket) {

    private static final ProductMapper productMapper = ProductMapper.INSTANCE;
    @PutMapping("/products/add")
    public ResponseEntity<ResponseDto> addProduct(
            @RequestParam("uuid") UUID uuid,
            @RequestParam("quantity") Optional<Integer> quantity
    ) {
        ProductDto dto = productMapper.toDto(bucketService.addProductToBucket(bucket, uuid, quantity.orElse(1)));
        return ResponseEntity.ok(ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(String.format("%s product(s) %s successfully added to the bucket", quantity.orElse(1), dto))
                .data(Map.of("uuid", dto.uuid()))
                .timeStamp(now())
                .build()
        );
    }

    @DeleteMapping("/products/remove")
    public ResponseEntity<ResponseDto> removeProduct(
            @RequestParam("uuid") UUID uuid,
            @RequestParam("quantity") Optional<Integer> quantity
    ) {
        Integer deletedQuantity = bucketService
                .removeProductFromBucket(bucket, uuid, quantity.orElse(1));

        return ResponseEntity.ok(ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("%s product(s) with id %s successfully deleted from the bucket", deletedQuantity, uuid))
                .data(Map.of("uuid", uuid))
                .timeStamp(now())
                .build()
        );
    }

    @GetMapping("/products")
    public ResponseEntity<ResponseDto> getProducts() {
        List<UUID> uuids = bucketService
                .getBucketProducts(bucket)
                .stream().map(Product::uuid).toList();

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Products in the bucket")
                        .timeStamp(now())
                        .data(Map.of("uuids", uuids))
                        .build()
        );
    }

    @GetMapping("/products/map")
    public ResponseEntity<ResponseDto> getProductsMap() {
        var productsMap = bucketService
                .getBucketProductsMap(bucket);


        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Products in the bucket")
                        .timeStamp(now())
                        .data(Map.of("products", productsMap))
                        .build()
        );
    }

    @GetMapping("/products/{uuid}/quantity")
    public ResponseEntity<ResponseDto> getQuantity(
            @PathVariable("uuid") UUID uuid
    ) {
        Integer quantity = requireNonNullElse(
                bucketService.getProductQuantity(bucket, uuid), 0);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Bucket count")
                        .timeStamp(now())
                        .data(Map.of("quantity", quantity))
                        .build()
        );
    }

    @PutMapping("/products/{uuid}/quantity")
    public ResponseEntity<ResponseDto> setQuantity(
            @PathVariable("uuid") UUID uuid,
            @RequestParam("quantity") Integer quantity
    ) {
        bucketService.setProductQuantity(bucket, uuid, quantity);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message(format("Product quantity with id %s set to %s", uuid, quantity))
                        .timeStamp(now())
                        .data(Map.of("quantity", quantity))
                        .build()
        );
    }
}
