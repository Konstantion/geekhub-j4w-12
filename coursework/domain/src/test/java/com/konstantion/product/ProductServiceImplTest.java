package com.konstantion.product;

import com.konstantion.category.Category;
import com.konstantion.category.CategoryPort;
import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.NonExistingIdException;
import com.konstantion.exception.ValidationException;
import com.konstantion.file.MultipartFileService;
import com.konstantion.product.model.CreateProductRequest;
import com.konstantion.product.model.GetProductsRequest;
import com.konstantion.product.model.UpdateProductRequest;
import com.konstantion.product.validator.ProductValidator;
import com.konstantion.user.Permission;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {
    @Mock
    ProductPort productPort;
    @Mock
    CategoryPort categoryPort;
    @Mock
    ProductValidator productValidator;
    @Mock
    MultipartFileService fileService;
    @Mock
    User user;
    @InjectMocks
    ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
    }

    @Test
    void shouldThrowForbiddenExceptionWhenMethodRequirePermissionWithoutPermission() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(true);

        assertThatThrownBy(() -> productService.activate(null, user))
                .isInstanceOf(ForbiddenException.class);

        assertThatThrownBy(() -> productService.deactivate(null, user))
                .isInstanceOf(ForbiddenException.class);

        assertThatThrownBy(() -> productService.create(null, user))
                .isInstanceOf(ForbiddenException.class);

        assertThatThrownBy(() -> productService.update(null, null, user))
                .isInstanceOf(ForbiddenException.class);

        assertThatThrownBy(() -> productService.delete(null, user))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenGetByIdWithNonExistingId() {
        UUID nonExistingId = UUID.randomUUID();
        when(productPort.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getById(nonExistingId))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldReturnProductWhenGetByIdWithExistingId() {
        when(productPort.findById(any(UUID.class))).thenReturn(Optional.of(Product.builder().build()));

        Product actual = productService.getById(UUID.randomUUID());

        assertThat(actual).isNotNull();
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenGetAllWithNonExistingCategoryId() {
        when(categoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getAll(new GetProductsRequest(1, 1, "", "", UUID.randomUUID(), false)))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenGetAllWithInvalidOrderBy() {
        when(categoryPort.findById(any(UUID.class))).thenReturn(Optional.of(Category.builder().build()));

        assertThatThrownBy(() -> productService.getAll(new GetProductsRequest(1, 1, "orderBy", "searchPattern", UUID.randomUUID(), false)))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldReturnAllProductsWhenGetAll() {
        when(categoryPort.findById(any(UUID.class))).thenReturn(Optional.of(Category.builder().build()));
        when(productPort.findAll(anyInt(), anyInt(), anyString(), anyString(), any(UUID.class), anyBoolean(), anyBoolean())).thenReturn(new PageImpl<>(List.of(Product.builder().build()), Pageable.ofSize(1), 1));
        Page<Product> products = productService.getAll(new GetProductsRequest(1, 1, "name", "searchPattern", UUID.randomUUID(), false), false);
        assertThat(products)
                .isNotNull()
                .extracting(Page::getContent)
                .matches(list -> list.size() == 1);
    }

    @Test
    void shouldCallMethodFindAllWithValidDataWhenGetAllWithInvalidPageData() {
        when(categoryPort.findById(any(UUID.class))).thenReturn(Optional.of(Category.builder().build()));
        when(productPort.findAll(anyInt(), anyInt(), anyString(), anyString(), any(UUID.class), anyBoolean(), anyBoolean())).thenReturn(new PageImpl<>(List.of(Product.builder().build()), Pageable.ofSize(1), 1));
        Page<Product> products = productService.getAll(new GetProductsRequest(-3124, -3333454, "name", "searchPattern", UUID.randomUUID(), false), false);
        assertThat(products)
                .isNotNull()
                .extracting(Page::getContent)
                .matches(list -> list.size() == 1);
        verify(productPort, times(1)).findAll(eq(1), eq(1), anyString(), anyString(), any(UUID.class), anyBoolean(), anyBoolean());
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenDeleteWithNonExistingId() {
        UUID nonExistingId = UUID.randomUUID();
        when(productPort.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deactivate(nonExistingId, user));
    }

    @Test
    void shouldDeleteProductWhenDeleteWithExistingId() {
        when(productPort.findById(any())).thenReturn(Optional.of(Product.builder().build()));

        Product deleted = productService.delete(UUID.randomUUID(), user);

        assertThat(deleted).isNotNull();

        verify(productPort, times(1)).delete(deleted);
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateOrUpdateWithInvalidData() {
        when(productValidator.validate(any(CreateProductRequest.class))).thenReturn(ValidationResult.invalid(Set.of()));
        when(productValidator.validate(any(UpdateProductRequest.class))).thenReturn(ValidationResult.invalid(Set.of()));

        assertThatThrownBy(() -> productService.create(new CreateProductRequest(null, null, null, null, null, null), user))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> productService.update(UUID.randomUUID(), new UpdateProductRequest(null, null, null, null, null, null), user))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldCreateProductWhenCreateWithValidData() {
        when(productValidator.validate(any(CreateProductRequest.class))).thenReturn(ValidationResult.valid());
        when(fileService.getFileBytes(any())).thenReturn(new byte[0]);

        Product product = productService.create(new CreateProductRequest("name", null, null, null, null, null), user);

        assertThat(product)
                .isNotNull()
                .extracting(Product::getName).isEqualTo("name");
    }

    @Test
    void shouldUpdateProductWhenUpdateWithValidData() {
        when(productValidator.validate(any(UpdateProductRequest.class))).thenReturn(ValidationResult.valid());
        when(fileService.getFileBytes(any())).thenReturn(new byte[0]);
        when(productPort.findById(any())).thenReturn(Optional.of(Product.builder().price(10.4).build()));

        Product updated = productService.update(UUID.randomUUID(), new UpdateProductRequest("name", null, null, null, null, null), user);

        assertThat(updated)
                .isNotNull()
                .matches(product -> product.getName().equals("name") && product.getPrice().equals(10.4));
    }

    @Test
    void shouldActivateTableWhenActivate() {
        when(productPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(Product.builder().active(false).build()))
                .thenReturn(Optional.of(Product.builder().active(true).build()));

        Product actualActivated = productService.activate(UUID.randomUUID(), user);
        Product actualActive = productService.activate(UUID.randomUUID(), user);

        Assertions.assertThat(actualActivated)
                .isNotNull()
                .extracting(Product::isActive).isEqualTo(true);
        Assertions.assertThat(actualActive)
                .isNotNull()
                .extracting(Product::isActive).isEqualTo(true);

        verify(productPort, times(1)).save(actualActivated);
    }

    @Test
    void shouldDeactivateTableWhenDeactivate() {
        when(productPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(Product.builder().active(true).build()))
                .thenReturn(Optional.of(Product.builder().active(false).build()));

        Product actualDeactivated = productService.deactivate(UUID.randomUUID(), user);
        Product actualInactive = productService.deactivate(UUID.randomUUID(), user);

        Assertions.assertThat(actualDeactivated)
                .isNotNull()
                .extracting(Product::isActive).isEqualTo(false);
        Assertions.assertThat(actualInactive)
                .isNotNull()
                .extracting(Product::isActive).isEqualTo(false);

        verify(productPort, times(1)).save(actualDeactivated);
    }
}