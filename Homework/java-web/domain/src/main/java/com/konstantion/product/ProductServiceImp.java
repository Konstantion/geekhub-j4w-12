package com.konstantion.product;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.review.Review;
import com.konstantion.review.ReviewMapper;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

import static java.lang.String.format;
import static org.springframework.data.domain.Sort.Direction.ASC;

public record ProductServiceImp(ProductValidator productValidator,
                                ProductRepository productRepository,
                                Logger logger)
        implements ProductService {

    public ProductServiceImp(ProductValidator productValidator,
                             ProductRepository productRepository) {
        this(productValidator, productRepository, LoggerFactory.getLogger(ProductServiceImp.class));
    }

    static ProductMapper productMapper = ProductMapper.INSTANCE;
    static ReviewMapper reviewMapper = ReviewMapper.INSTANCE;

    @Override
    public ProductDto create(CreationProductDto createProductDto) {
        ValidationResult validationResult = productValidator
                .validate(createProductDto);

        if (validationResult.errorsPresent()) {
            throw new ValidationException("Failed to create product, given data is invalid",
                    validationResult.getErrorsAsMap());
        }

        Product product = productMapper.toEntity(createProductDto);

        product = product
                .setCreatedAt(LocalDateTime.now())
                .setUuid(UUID.randomUUID());

        product = productRepository.saveAndFlush(product);

        ProductDto productDto = productMapper.toDto(product);

        logger.info("Product {} successfully created", productDto);

        return productDto;
    }

    @Override
    public ProductDto delete(UUID uuid) {
        Product product = productRepository.findByUuid(uuid).orElseThrow(() ->
                new BadRequestException(format("Product with uuid %s doesn't exist", uuid)
                ));

        productRepository.deleteByUuid(uuid);

        logger.info("Product with id {} successfully delete", uuid);
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductDto> getAll() {
        return getAll(ASC, "name");
    }

    public List<Map.Entry<ProductDto, Double>> getAllWithRating() {
        return productRepository.findAllProductsWithReviews()
                .entrySet()
                .stream()
                .map(entry -> {
                    double reviewCount = entry.getValue().size();
                    double reviewSum = entry.getValue().stream().map(Review::rating).reduce(0, Integer::sum);
                    return Map.entry(productMapper.toDto(entry.getKey()), reviewSum / reviewCount);
                }).sorted(Comparator.comparingDouble(Map.Entry::getValue)).toList();
    }

    public List<ProductDto> getAllWithReview() {
        return productRepository
                .findAllProductsWithReviews().entrySet().stream()
                .map(e -> {
                    ProductDto productDto = productMapper.toDto(e.getKey());
                    Double rating = e.getValue().stream().mapToDouble(Review::rating).average().orElse(0.0);
                    return productDto.setReviews(reviewMapper.toDto(e.getValue())).setRating(rating);
                }).toList();
    }

    @Override
    public List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName) {
        List<Product> products = productRepository.findAll(Sort.by(sortOrder, fieldName));
        return productMapper.toDto(products);
    }
}
