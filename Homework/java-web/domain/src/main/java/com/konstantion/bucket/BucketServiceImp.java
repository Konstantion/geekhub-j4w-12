package com.konstantion.bucket;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.product.ProductMapper;
import com.konstantion.product.ProductRepository;
import com.konstantion.product.ProductService;
import com.konstantion.product.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

public record BucketServiceImp(ProductRepository productRepository, ProductService productService)
        implements BucketService {

    private static final String PRODUCT_WITH_UUID_DOESNT_EXIST = "Product with uuid %s doesn't exist";
    private static final ProductMapper productMapper = ProductMapper.INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(BucketServiceImp.class);

    @Override
    public Integer removeProductFromBucket(Bucket bucket, UUID productUuid, Integer quantity) {
        productService.getById(productUuid);
        int counter = 0;
        for (; counter < quantity; counter++) {
            boolean removed = bucket.removeProduct(productUuid);
            if (!removed) {
                logger.info("Product with id {} can't be removed {}", productUuid, bucket);
                break;
            }
        }
        logger.info("Quantity {} product with id {} removed from bucket", counter, productUuid);

        return counter;
    }

    @Override
    public List<ProductDto> getBucketProducts(Bucket bucket) {
        return bucket.products().keySet().stream()
                .map(productRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public Integer getProductQuantity(Bucket bucket, UUID productUuid) {
        productService.getById(productUuid);
        if (!bucket.products().containsKey(productUuid)) {
            return 0;
        }
        return bucket.products().get(productUuid);
    }

    @Override
    public Map<UUID, Integer> getBucketProductsMap(Bucket bucket) {
        return bucket.products();
    }

    @Override
    public boolean setProductQuantity(Bucket bucket, UUID productUuid, Integer quantity) {
        productService.getById(productUuid);
        if (isNull(quantity) || quantity < 0) {
            throw new BadRequestException("Product quantity should be positive value");
        }

        bucket.setProductQuantity(productUuid, quantity);
        return true;
    }

    @Override
    public ProductDto addProductToBucket(Bucket bucket, UUID productUuid, Integer quantity) {
        ProductDto dto = productService.getById(productUuid);
        for (int i = 0; i < quantity; i++) {
            bucket.addProduct(productUuid);
        }

        logger.info("Quantity {} of product with id {} added to bucket", quantity, productUuid);

        return dto;
    }
}
