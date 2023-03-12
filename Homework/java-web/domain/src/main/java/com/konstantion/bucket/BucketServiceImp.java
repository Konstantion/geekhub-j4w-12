package com.konstantion.bucket;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.product.Product;
import com.konstantion.product.ProductMapper;
import com.konstantion.product.ProductRepository;
import com.konstantion.product.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

public record BucketServiceImp(ProductRepository productRepository) implements BucketService {

    private static final String PRODUCT_WITH_UUID_DOESNT_EXIST = "Product with uuid %s doesn't exist";
    private static final ProductMapper productMapper = ProductMapper.INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(BucketServiceImp.class);

    @Override
    public Integer removeProductFromBucket(Bucket bucket, UUID productUuid, Integer quantity) {
        Product product = productRepository.findById(productUuid).orElseThrow(
                () -> new BadRequestException(format(
                        PRODUCT_WITH_UUID_DOESNT_EXIST,
                        productUuid)
                ));
        int counter = 0;
        for (; counter < quantity; counter++) {
            boolean removed = bucket.removeProduct(product);
            if (!removed) {
                logger.info("{} can't be removed {}", productMapper.toDto(product), bucket);
                break;
            }
        }
        logger.info("{} {} removed from bucket", counter, productMapper.toDto(product));

        return counter;
    }

    @Override
    public List<ProductDto> getBucketProducts(Bucket bucket) {
        return bucket.products().keySet().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public Integer getProductQuantity(Bucket bucket, UUID productUuid) {
        Product product = productRepository.findById(productUuid).orElseThrow(
                () -> new BadRequestException(format(
                        PRODUCT_WITH_UUID_DOESNT_EXIST,
                        productUuid)
                ));

        return bucket.products().get(product);
    }

    @Override
    public ProductDto addProductToBucket(Bucket bucket, UUID productUuid, Integer quantity) {
        Product product = productRepository.findById(productUuid).orElseThrow(
                () -> new BadRequestException(format(
                        PRODUCT_WITH_UUID_DOESNT_EXIST,
                        productUuid)
                ));
        for (int i = 0; i < quantity; i++) {
            bucket.addProduct(product);
        }

        logger.info("{} product {} added to bucket", quantity, product);

        return productMapper.toDto(product);
    }
}
