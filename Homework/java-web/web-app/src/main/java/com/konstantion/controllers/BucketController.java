package com.konstantion.controllers;

import com.konstantion.bucket.Bucket;
import com.konstantion.bucket.BucketService;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.dto.ProductQuantityDto;
import com.konstantion.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("bucket")
public record BucketController(BucketService bucketService, Bucket bucket) {

    @GetMapping()
    public String getView() {
        return "bucket";
    }

    @PutMapping("/add")
    public ResponseEntity<Response> addProduct(
            @RequestParam UUID productUuid,
            @RequestParam Optional<Integer> quantity
    ) {
        ProductDto dto = bucketService.addProductQuantityToBucket(bucket, productUuid, quantity.orElse(1));
        return ResponseEntity.ok(Response.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("%s product(s) %s successfully added to the bucket", quantity.orElse(1), dto.name()))
                .timeStamp(now())
                .build()
        );
    }

    @GetMapping("/products")
    public ResponseEntity<Response> getProducts() {
        List<ProductQuantityDto> products = bucketService
                .getBucketProductsList(bucket);

        return ResponseEntity.ok(
                Response.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Products in the bucket")
                        .timeStamp(now())
                        .data(Map.of("products", products))
                        .build()
        );
    }

    @GetMapping("/bucket")
    public ResponseEntity<Response> getBucket() {
        return ResponseEntity.ok(
                Response.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Your bucket")
                        .timeStamp(now())
                        .data(Map.of("bucket", bucket))
                        .build()
        );
    }

    @GetMapping("/count")
    public ResponseEntity<Response> getCount() {
        return ResponseEntity.ok(
                Response.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Bucket count")
                        .timeStamp(now())
                        .data(Map.of("count", bucket.count()))
                        .build()
        );
    }

}
