package com.konstantion.product;

import com.konstantion.category.Category;
import com.konstantion.category.CategoryPort;
import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.file.MultipartFileService;
import com.konstantion.product.model.CreateProductRequest;
import com.konstantion.product.model.GetProductsRequest;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.CREATE_PRODUCT;
import static com.konstantion.user.Permission.DELETE_PRODUCT;
import static com.konstantion.user.Role.ADMIN;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;

@Component
public record ProductServiceImpl(
        ProductPort productPort,
        CategoryPort categoryPort,
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
    public Page<Product> getAll(GetProductsRequest request) {
        UUID categoryId = request.categoryId();
        if (nonNull(categoryId)) {
            categoryPort.findById(categoryId)
                    .orElseThrow(nonExistingIdSupplier(Category.class, categoryId));
        }

        String orderBy = request.orderBy().toLowerCase();
        if (!isOrderByValid(orderBy)) {
            throw new BadRequestException(format("Order by %s isn't provide", orderBy));
        }

        String searchPattern = request.searchPattern().trim();
        int pageNumber = Math.max(request.pageNumber(), 1);
        int pageSize = Math.max(request.pageSize(), 1);
        boolean ascending = request.ascending();

        return productPort.findAll(
                pageNumber,
                pageSize,
                orderBy,
                searchPattern,
                categoryId,
                ascending
        );
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

    private boolean isOrderByValid(String orderBy) {
        if (orderBy.isBlank()) {
            orderBy = "name";
        }

        List<String> validOrderBy = List.of("name", "price", "weight");
        return validOrderBy.contains(orderBy);
    }
}
