package com.konstantion.bucket;

import com.konstantion.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public record DomainBucketService(Logger logger) implements BucketService {

    public DomainBucketService() {
        this(LoggerFactory.getLogger(DomainBucketService.class));
    }

    @Override
    public void addProductToBucket(Bucket bucket, Product product) {
        bucket.addProduct(product);

        logger.info("{} added to {}", product, bucket);
    }

    @Override
    public void addProductsToBucket(Bucket bucket, List<Product> products) {
        for (Product product : products) {
            bucket.addProduct(product);
        }

        logger.info("{} added to {}", products, bucket);
    }

    @Override
    public void removeProductFromBucket(Bucket bucket, Product product) {
        boolean removed = bucket.removeProduct(product);
        if (removed) {
            logger.info("{} removed {}", product, bucket);
        }
        logger.info("{} can't be removed {}", product, bucket);
    }
}
