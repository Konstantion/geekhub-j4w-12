package com.konstantion.bucket;

import com.konstantion.product.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BucketService {
    default Product addProductToBucket(Bucket bucket, UUID productUuid) {
        return addProductToBucket(bucket, productUuid, 1);
    }

    Product addProductToBucket(Bucket bucket, UUID productUuid, Integer quantity);

    default Integer removeProductFromBucket(Bucket bucket, UUID productUuid) {
        return removeProductFromBucket(bucket, productUuid, 1);
    }

    Integer removeProductFromBucket(Bucket bucket, UUID productUuid, Integer qiantity);

    List<Product> getBucketProducts(Bucket bucket);

    Integer getProductQuantity(Bucket bucket, UUID productUuid);

    Map<UUID, Integer> getBucketProductsMap(Bucket bucket);

    boolean setProductQuantity(Bucket bucket, UUID productUuid, Integer quantity);
}
