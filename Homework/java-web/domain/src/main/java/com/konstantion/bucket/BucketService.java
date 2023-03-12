package com.konstantion.bucket;

import com.konstantion.product.Product;
import com.konstantion.product.dto.ProductDto;

import java.util.List;
import java.util.UUID;

public interface BucketService {
    default ProductDto addProductToBucket(Bucket bucket, UUID productUuid) {
        return addProductToBucket(bucket, productUuid, 1);
    }
    ProductDto addProductToBucket(Bucket bucket, UUID productUuid, Integer quantity);

    default Integer removeProductFromBucket(Bucket bucket, UUID productUuid) {
        return removeProductFromBucket(bucket, productUuid, 1);
    }

    Integer removeProductFromBucket(Bucket bucket, UUID productUuid, Integer qiantity);

    List<ProductDto> getBucketProducts(Bucket bucket);

    Integer getProductQuantity(Bucket bucket, UUID productUuid);
}
