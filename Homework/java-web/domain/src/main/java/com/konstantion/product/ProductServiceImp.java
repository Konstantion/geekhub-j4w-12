package com.konstantion.product;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.file.MultipartFileValidator;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.review.ReviewMapper;
import com.konstantion.upload.UploadService;
import com.konstantion.utils.validator.ValidationResult;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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
        byte[] imageBytes = uploadService.getFileBytes(file);

        product = product
                .setCreatedAt(LocalDateTime.now())
                .setImageBytes(imageBytes);

        product = productRepository.save(product);

        ProductDto productDto = productMapper.toDto(product);

        logger.info("Product {} successfully created", productDto);

        return productDto;
    }

    @Override
    public ProductDto getById(UUID uuid) {
        Product product = productRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Product with uuid %s doesn't exist", uuid)
        ));


        logger.info("Product with id {} successfully returned", uuid);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto delete(UUID uuid) {
        Product product = productRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Product with uuid %s doesn't exist", uuid)
                ));

        productRepository.deleteById(uuid);

        logger.info("Product with id {} successfully delete", uuid);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto update(UUID uuid, ProductDto productDto) {
        productRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Product with uuid %s doesn't exist", uuid)
                ));
        Product product = productMapper.toEntity(productDto)
                .setUuid(uuid);

        product = productRepository.save(product);

        logger.info("Product with uuid {} successfully updated", uuid);
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
                .findAll().stream()
                .map(productMapper::toDto)
                .sorted(getComparator(Sort.by(sortOrder, fieldName)))
                .filter(dto -> containsIgnoreCase(dto.name(), namePattern)).toList();
    }

    @Override
    public String getProductImage(UUID uuid) {
        Product product = productRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Product with uuid %s doesn't exist", uuid)
                ));
        String base64Encoded = Base64.encodeBase64String(product.imageBytes());
        return  "data:image/png;base64," + base64Encoded;
    }

    public Comparator<ProductDto> getComparator(Sort sort) {
        Comparator<ProductDto> comparator;
        Sort.Order order = sort.iterator().next();
        comparator = switch (order.getProperty()) {
            case "price" -> Comparator.comparing(ProductDto::price);
            case "name" -> Comparator.comparing(ProductDto::name);
            default -> Comparator.comparing(ProductDto::price);
        };

        if (order.getDirection().equals(Sort.Direction.DESC)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
