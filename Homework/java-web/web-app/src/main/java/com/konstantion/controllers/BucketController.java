package com.konstantion.controllers;

import com.konstantion.bucket.Bucket;
import com.konstantion.bucket.BucketService;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.response.Response;
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
@RequestMapping("/web-api/bucket")
public record BucketController(BucketService bucketService, Bucket bucket) {

    @PutMapping("/products")
    public ResponseEntity<Response> addProduct(
            @RequestParam("uuid") UUID uuid,
            @RequestParam("quantity") Optional<Integer> quantity
    ) {
        ProductDto dto = bucketService.addProductToBucket(bucket, uuid, quantity.orElse(1));
        return ResponseEntity.ok(Response.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("%s product(s) %s successfully added to the bucket", quantity.orElse(1), dto))
                .data(Map.of("uuid", dto.uuid()))
                .timeStamp(now())
                .build()
        );
    }

    @DeleteMapping("/products/{uuid}")
    public ResponseEntity<Response> deleteProduct(
            @PathVariable("uuid") UUID uuid,
            @RequestParam("quantity") Optional<Integer> quantity
    ) {
        Integer deletedQuantity = bucketService
                .removeProductFromBucket(bucket, uuid, quantity.orElse(1));

        return ResponseEntity.ok(Response.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("%s product(s) with id %s successfully deleted from the bucket", deletedQuantity, uuid))
                .data(Map.of("uuid", uuid))
                .timeStamp(now())
                .build()
        );
    }

    @GetMapping("/products")
    public ResponseEntity<Response> getProducts() {
        List<UUID> uuids = bucketService
                .getBucketProducts(bucket)
                .stream().map(ProductDto::uuid).toList();

        return ResponseEntity.ok(
                Response.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Products in the bucket")
                        .timeStamp(now())
                        .data(Map.of("uuids", uuids))
                        .build()
        );
    }

    @GetMapping("/products/{uuid}/quantity")
    public ResponseEntity<Response> getQuantity(
            @PathVariable("uuid") UUID uuid
    ) {
        Integer quantity = requireNonNullElse(
                bucketService.getProductQuantity(bucket, uuid), 0);

        return ResponseEntity.ok(
                Response.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Bucket count")
                        .timeStamp(now())
                        .data(Map.of("quantity", quantity))
                        .build()
        );
    }
}
