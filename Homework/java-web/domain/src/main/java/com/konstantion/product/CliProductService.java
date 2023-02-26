package com.konstantion.product;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.data.domain.Sort.Direction.ASC;

public record CliProductService(ProductValidator productValidator,
                                ProductRepository productRepository,
                                Logger logger)
        implements ProductService {

    public CliProductService(ProductValidator productValidator,
                             ProductRepository productRepository) {
        this(productValidator, productRepository, LoggerFactory.getLogger(CliProductService.class));
    }

    static ProductMapper MAPPER = ProductMapper.INSTANCE;

    @Override
    public ProductDto create(CreationProductDto createProductDto) {
        ValidationResult validationResult = productValidator
                .validate(createProductDto);

        if (validationResult.errorsPresent()) {
            throw new ValidationException("Failed to create product, given data is invalid",
                    validationResult.getErrorsAsMap());
        }

        Product product = MAPPER.toEntity(createProductDto);

        product = product
                .setCreatedAt(LocalDateTime.now())
                .setUuid(UUID.randomUUID());

        product = productRepository.saveAndFlush(product);

        ProductDto productDto = MAPPER.toDto(product);

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
        return MAPPER.toDto(product);
    }

    @Override
    public List<ProductDto> getAll() {
        return getAll(ASC, "name");
    }

    @Override
    public List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName) {
        List<Product> products = productRepository.findAll(Sort.by(sortOrder, fieldName));

        return MAPPER.toDto(products);
    }
}
