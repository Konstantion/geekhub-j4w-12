package com.konstantion.product;

import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.file.MultipartFileService;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.konstantion.user.Permission.CREATE_PRODUCT;
import static com.konstantion.user.Permission.DELETE_PRODUCT;
import static com.konstantion.user.Role.ADMIN;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Component
public record ProductServiceImpl(
        ProductRepository productRepository,
        ProductValidator productValidator,
        MultipartFileService fileService
) implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private static final ProductMapper productMapper = ProductMapper.INSTANCE;



    @Override
    public ProductDto create(CreationProductDto cpdto, User user) {
        if (user.hasNoPermission(CREATE_PRODUCT)) {
            throw new ForbiddenException("Not enough authorities to create product");
        }

        ValidationResult validationResult = productValidator
                .validate(cpdto);
        validationResult.validOrTrow();

        byte[] imageBytes = fileService.getFileBytes(cpdto.image());

        Product product = Product.builder()
                .name(cpdto.name())
                .price(cpdto.price())
                .description(cpdto.description())
                .weight(cpdto.weight())
                .active(true)
                .creatorId(user.getId())
                .createdAt(now())
                .categoryId(cpdto.categoryId())
                .imageBytes(imageBytes)
                .build();

        productRepository.save(product);

        logger.info("Product {} successfully created", product);

        return productMapper.toDto(product);
    }

    @Override
    public ProductDto delete(UUID productId, User user) {
        if (user.hasNoPermission(DELETE_PRODUCT)) {
            throw new ForbiddenException("Not enough authorities to delete product");
        }

        Product product = getByIdOrThrow(productId);

        productRepository.delete(product);

        logger.info("Product {} successfully deleted", product);

        return productMapper.toDto(product);
    }

    @Override
    public ProductDto deactivate(UUID productId, User user) {
        if (user.hasNoPermission(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to deactivate product");
        }

        Product product = getByIdOrThrow(productId);

        if (!product.getActive()) {
            logger.info("Product {} is already disabled", product);
            return productMapper.toDto(product);
        }

        prepareToDeactivate(product);
        productRepository.save(product);

        logger.info("Product {} successfully deactivated", product);

        return productMapper.toDto(product);
    }

    @Override
    public ProductDto activate(UUID productId, User user) {
        if (user.hasNoPermission(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to activate product");
        }

        Product product = getByIdOrThrow(productId);

        if (product.getActive()) {
            logger.info("Product {} is already activated", product);
            return productMapper.toDto(product);
        }

        prepareToActivate(product);
        productRepository.save(product);

        logger.info("Product {} successfully activated", product);

        return productMapper.toDto(product);
    }

    @Override
    public ProductDto getById(UUID productId) {
        Product product = getByIdOrThrow(productId);
        return productMapper.toDto(product);
    }

    private void prepareToDeactivate(Product product) {
        product.setDeactivateAt(now());
        product.setActive(false);
    }

    private void prepareToActivate(Product product) {
        product.setDeactivateAt(null);
        product.setActive(true);
    }

    private Product getByIdOrThrow(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> {
            throw new BadRequestException(format("Product with id %s doesn't exist", productId));
        });
    }
}
