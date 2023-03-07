package com.konstantion.product;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.review.Review;
import com.konstantion.review.ReviewMapper;
import com.konstantion.upload.UploadService;
import com.konstantion.utils.validator.ValidationResult;
import com.konstantion.file.MultipartFileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.springframework.data.domain.Sort.Direction.ASC;

public record ProductServiceImp(ProductValidator productValidator,
                                MultipartFileValidator fileValidator,
                                ProductRepository productRepository,

                                UploadService uploadService,
                                Logger logger)
        implements ProductService {

    public ProductServiceImp(ProductValidator productValidator,
                             MultipartFileValidator fileValidator,
                             ProductRepository productRepository,
                             UploadService uploadService) {
        this(productValidator, fileValidator, productRepository, uploadService, LoggerFactory.getLogger(ProductServiceImp.class));
    }

    static ProductMapper productMapper = ProductMapper.INSTANCE;
    static ReviewMapper reviewMapper = ReviewMapper.INSTANCE;

    @Override
    public ProductDto create(CreationProductDto createProductDto, MultipartFile file) {
        ValidationResult validationResult = productValidator
                .validate(createProductDto)
                .combine(fileValidator.validate(file));


        if (validationResult.errorsPresent()) {
            throw new ValidationException("Failed to create product, given data is invalid",
                    validationResult.getErrorsAsMap()
            );
        }

        Product product = productMapper.toEntity(createProductDto);
        String imagePath = uploadService.uploadProductImage(file);

        product = product
                .setCreatedAt(LocalDateTime.now())
                .setUuid(UUID.randomUUID())
                .setImagePath(imagePath);

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

    @Override
    public List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName) {
        return getAll(sortOrder, fieldName, "");
    }

    @Override
    public List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName, String namePattern) {
        return productRepository
                .findAllProductsWithReviews().entrySet().stream()
                .map(e -> {
                    ProductDto productDto = productMapper.toDto(e.getKey());
                    Double rating = e.getValue().stream()
                            .mapToDouble(Review::rating)
                            .average().orElse(0.0);
                    return productDto.setReviews(reviewMapper.toDto(e.getValue()))
                            .setRating(rating);
                })
                .sorted(getComparator(Sort.by(sortOrder, fieldName)))
                .filter(dto -> containsIgnoreCase(dto.name(), namePattern)).toList();
    }

    public Comparator<ProductDto> getComparator(Sort sort) {
        Comparator<ProductDto> comparator;
        Sort.Order order = sort.iterator().next();
        comparator = switch (order.getProperty()) {
            case "price" -> Comparator.comparing(ProductDto::price);
            case "name" -> Comparator.comparing(ProductDto::name);
            default -> Comparator.comparing(ProductDto::rating);
        };

        if (order.getDirection().equals(Sort.Direction.DESC)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
