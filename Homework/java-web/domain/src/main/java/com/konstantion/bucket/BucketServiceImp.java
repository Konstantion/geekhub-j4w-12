package com.konstantion.bucket;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.product.Product;
import com.konstantion.product.ProductMapper;
import com.konstantion.product.ProductRepository;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.dto.ProductQuantityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

public record BucketServiceImp(Logger logger, ProductRepository productRepository) implements BucketService {

    private static final String PRODUCT_WITH_UUID_DOESNT_EXIST = "Product with uuid %s doesn't exist";
    private static final ProductMapper productMapper = ProductMapper.INSTANCE;


    public BucketServiceImp(ProductRepository productRepository) {
        this(LoggerFactory.getLogger(BucketServiceImp.class), productRepository);
    }

    @Override
    public void addProductToBucket(Bucket bucket, UUID productUuid) {
        Product product = productRepository.findByUuid(productUuid).orElseThrow(
                () -> new BadRequestException(format(
                        PRODUCT_WITH_UUID_DOESNT_EXIST,
                        productUuid)
                ));
        bucket.addProduct(product);

        logger.info("{} added to {}", productMapper.toDto(product), bucket);
    }

    @Override
    public void addProductsToBucket(Bucket bucket, List<Product> products) {
        for (Product product : products) {
            bucket.addProduct(product);
        }

        logger.info("{} added to {}", products, bucket);
    }

    @Override
    public void removeProductFromBucket(Bucket bucket, UUID productUuid) {
        Product product = productRepository.findByUuid(productUuid).orElseThrow(
                () -> new BadRequestException(format(
                        PRODUCT_WITH_UUID_DOESNT_EXIST,
                        productUuid)
                ));
        boolean removed = bucket.removeProduct(product);
        if (!removed) {
            logger.info("{} can't be removed {}", productMapper.toDto(product), bucket);
        }
        logger.info("{} removed {}", productMapper.toDto(product), bucket);
    }

    @Override
    public Map<ProductDto, Integer> getBucketProductsMap(Bucket bucket) {
        return bucket.products()
                .entrySet()
                .stream()
                .map(entry -> Map.entry(productMapper.toDto(entry.getKey()), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<ProductQuantityDto> getBucketProductsList(Bucket bucket) {
        return bucket.products()
                .entrySet()
                .stream()
                .map(entry -> Map.entry(productMapper.toDto(entry.getKey()), entry.getValue()))
                .map(ProductQuantityDto::fromEntry)
                .toList();
    }

    @Override
    public ProductDto addProductQuantityToBucket(Bucket bucket, UUID productUuid, Integer quantity) {
        Product product = productRepository.findByUuid(productUuid).orElseThrow(
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
