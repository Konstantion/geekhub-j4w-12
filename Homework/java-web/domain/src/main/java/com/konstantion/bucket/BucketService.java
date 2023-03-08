package com.konstantion.bucket;

import com.konstantion.product.Product;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.dto.ProductQuantityDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BucketService {
    void addProductToBucket(Bucket bucket, UUID productUuid);

    void addProductsToBucket(Bucket bucket, List<Product> products);

    ProductDto addProductQuantityToBucket(Bucket bucket, UUID productUuid, Integer quantity);

    void removeProductFromBucket(Bucket bucket, UUID productUuid);

    Map<ProductDto, Integer> getBucketProductsMap(Bucket bucket);

    List<ProductQuantityDto> getBucketProductsList(Bucket bucket);
}
