package com.konstantion.product;

import com.konstantion.category.Category;
import com.konstantion.category.CategoryPort;
import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.file.MultipartFileService;
import com.konstantion.product.model.CreateProductRequest;
import com.konstantion.product.model.GetProductsRequest;
import com.konstantion.product.model.UpdateProductRequest;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.*;
import static com.konstantion.utils.ObjectUtils.requireNonNullOrElseNullable;
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
        if (user.hasNoPermission(CREATE_PRODUCT)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
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

        logger.info("Product successfully created and returned");
        return product;
    }

    @Override
    public Page<Product> getAll(GetProductsRequest request, boolean onlyActive) {
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

        Page<Product> products = productPort.findAll(
                pageNumber,
                pageSize,
                orderBy,
                searchPattern,
                categoryId,
                ascending,
                onlyActive
        );
        logger.info("All products successfully returned");
        return products;
    }

    @Override
    public Product delete(UUID productId, User user) {
        if (user.hasNoPermission(DELETE_PRODUCT)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Product product = getByIdOrThrow(productId);

        productPort.delete(product);

        logger.info("Product with id {} successfully deleted and returned", productId);
        return product;
    }

    @Override
    public Product update(UUID productId, UpdateProductRequest request, User user) {
        if (user.hasNoPermission(CREATE_PRODUCT)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        productValidator.validate(request).validOrTrow();

        Product product = getByIdOrThrow(productId);

        UUID categoryId = request.categoryId();
        if (nonNull(categoryId)) {
            categoryPort.findById(categoryId)
                    .orElseThrow(nonExistingIdSupplier(Category.class, categoryId));
        }

        MultipartFile file = request.image();
        byte[] imageBytes = null;
        if (nonNull(file)) {
            imageBytes = fileService.getFileBytes(file);
        }

        updateProduct(product, request, imageBytes);

        productPort.save(product);

        logger.info("Product with id {} successfully updated and returned", productId);
        return product;
    }

    @Override
    public Product deactivate(UUID productId, User user) {
        if (user.hasNoPermission(CHANGE_PRODUCT_STATE)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Product product = getByIdOrThrow(productId);

        if (!product.isActive()) {
            logger.info("Product with id {} is already inactive", productId);
            return product;
        }

        prepareToDeactivate(product);
        productPort.save(product);

        logger.info("Product with id {} successfully deactivated and returned", product);

        return product;
    }

    @Override
    public Product activate(UUID productId, User user) {
        if (user.hasNoPermission(CHANGE_PRODUCT_STATE)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Product product = getByIdOrThrow(productId);

        if (product.isActive()) {
            logger.warn("Product with id {} is already active", productId);
            return product;
        }

        prepareToActivate(product);
        productPort.save(product);

        logger.info("Product with id {} successfully activated and returned", productId);
        return product;
    }

    @Override
    public Product getById(UUID productId) {
        Product product = getByIdOrThrow(productId);
        logger.info("Product with id {} successfully returned", productId);
        return product;
    }

    private void updateProduct(Product product, UpdateProductRequest request, byte[] imageBytes) {
        product.setName(requireNonNullOrElseNullable(request.name(), product.getName()));
        product.setPrice(requireNonNullOrElseNullable(request.price(), product.getPrice()));
        product.setWeight(requireNonNullOrElseNullable(request.weight(), product.getWeight()));
        product.setDescription(requireNonNullOrElseNullable(request.description(), product.getDescription()));
        product.setCategoryId(requireNonNullOrElseNullable(request.categoryId(), null));
        product.setImageBytes(requireNonNullOrElseNullable(imageBytes, product.getImageBytes()));
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

        List<String> validOrderBy = List.of("name", "price", "weight");
        return validOrderBy.contains(orderBy);
    }
}
