package com.konstantion.product;

import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.file.MultipartFileService;
import com.konstantion.product.model.CreateProductRequest;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.CREATE_PRODUCT;
import static com.konstantion.user.Permission.DELETE_PRODUCT;
import static com.konstantion.user.Role.ADMIN;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Component
public record ProductServiceImpl(
        ProductPort productPort,
        ProductValidator productValidator,
        MultipartFileService fileService
) implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public Product create(CreateProductRequest createProductRequest, User user) {
        if (user.hasNoPermission(CREATE_PRODUCT)) {
            throw new ForbiddenException("Not enough authorities to create product");
        }

        ValidationResult validationResult = productValidator
                .validate(createProductRequest);
        validationResult.validOrTrow();

        byte[] imageBytes = fileService.getFileBytes(createProductRequest.image());

        Product product = Product.builder()
                .name(createProductRequest.name())
                .price(createProductRequest.price())
                .description(createProductRequest.description())
                .weight(createProductRequest.weight())
                .active(true)
                .creatorId(user.getId())
                .createdAt(now())
                .categoryId(createProductRequest.categoryId())
                .imageBytes(imageBytes)
                .build();

        productPort.save(product);

        logger.info("Product {} successfully created", product);

        return product;
    }

    @Override
    public Product delete(UUID productId, User user) {
        if (user.hasNoPermission(DELETE_PRODUCT)) {
            throw new ForbiddenException("Not enough authorities to delete product");
        }

        Product product = getByIdOrThrow(productId);

        productPort.delete(product);

        logger.info("Product {} successfully deleted", product);

        return product;
    }

    @Override
    public Product deactivate(UUID productId, User user) {
        if (user.hasNoPermission(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to deactivate product");
        }

        Product product = getByIdOrThrow(productId);

        if (!product.getActive()) {
            logger.info("Product {} is already disabled", product);
            return product;
        }

        prepareToDeactivate(product);
        productPort.save(product);

        logger.info("Product {} successfully deactivated", product);

        return product;
    }

    @Override
    public Product activate(UUID productId, User user) {
        if (user.hasNoPermission(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to activate product");
        }

        Product product = getByIdOrThrow(productId);

        if (product.getActive()) {
            logger.info("Product {} is already activated", product);
            return product;
        }

        prepareToActivate(product);
        productPort.save(product);

        logger.info("Product {} successfully activated", product);

        return product;
    }

    @Override
    public Product getById(UUID productId) {
        return getByIdOrThrow(productId);
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
        return productPort.findById(productId)
                .orElseThrow(nonExistingIdSupplier(Product.class, productId));
    }
}
