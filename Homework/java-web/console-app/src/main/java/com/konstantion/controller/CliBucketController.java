package com.konstantion.controller;

import com.konstantion.bucket.Bucket;
import com.konstantion.bucket.BucketService;
import com.konstantion.product.dto.ProductDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.UUID;

@Component
public record CliBucketController(Bucket bucket, BucketService bucketService) {

    public void addProduct(UUID productUuid, Integer quantity) {
        bucketService.addProductQuantityToBucket(bucket, productUuid, quantity);
    }

    public void removeProduct(UUID productUuid) {
        bucketService.removeProductFromBucket(bucket, productUuid);
    }

    public Map<ProductDto, Integer> getBucketProducts() {
        return bucketService.getBucketProducts(bucket);
    }
}
