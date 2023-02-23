package com.konstantion.bucket;

import com.konstantion.product.Product;

import java.util.List;

public interface BucketService {
    void addProductToBucket(Bucket bucket, Product product);

    void addProductsToBucket(Bucket bucket, List<Product> products);

    void addProductCountToBucket(Bucket bucket, Product product, Integer count);

    void removeProductFromBucket(Bucket bucket, Product product);
}
