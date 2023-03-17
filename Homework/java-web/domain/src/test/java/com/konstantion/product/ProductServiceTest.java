package com.konstantion.product;

import com.github.javafaker.Faker;
import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.validator.ProductValidations;
import com.konstantion.product.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

class ProductServiceTest {
    private ProductRepository repository;
    private ProductValidations productValidations;
    private ProductValidator validator;
    private ProductServiceImp service;
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productValidations = new ProductValidations();
        validator = spy(new ProductValidator(productValidations));
        repository = mock(ProductRepository.class);
        service = new ProductServiceImp(validator, null, repository, null, null);
        productMapper = ProductMapper.INSTANCE;
    }

    @Test
    void process_shouldReturnProductId_whenCreateProduct() {
        CreationProductDto creationProductDto = new CreationProductDto("Bread", 1.0, null, null, null);
        UUID firstUuid = UUID.randomUUID();
        UUID secondUuid = UUID.randomUUID();
        Product productWithIdOne = Product.builder().uuid(firstUuid).build();
        Product productWithIdTwo = Product.builder().uuid(secondUuid).build();

        when(repository.saveAndFlush(any(Product.class)))
                .thenReturn(productWithIdOne)
                .thenReturn(productWithIdTwo);

        ProductDto firstProductDto = service.create(creationProductDto);
        ProductDto secondProductDto = service.create(creationProductDto);

        assertThat(firstProductDto.uuid()).isEqualTo(firstUuid);
        assertThat(secondProductDto.uuid()).isEqualTo(secondUuid);
    }

    @Test
    void process_shouldThrowValidationException_whenProductIsInvalname() {
        CreationProductDto creationProductDto = new CreationProductDto("", 1.0, null, null, null);

        assertThatThrownBy(() -> service.create(creationProductDto))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void process_shouldReturnSortedProducts_whenGetAllByDescName() {
        List<Product> dbData = new ArrayList<>();
        List<ProductDto> expectedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = Faker.instance().name().firstName().repeat(2);
            Double price = Faker.instance().number().randomDouble(2, 1, 100);
            LocalDateTime now = LocalDateTime.now();
            UUID uuid = UUID.randomUUID();
            ProductDto productDto = new ProductDto(uuid, name, price, null, null, now, null, null);
            Product product = new Product(uuid, name, price, null, null, now, null, null);

            dbData.add(product);
            expectedArray.add(productDto);
        }
        dbData = dbData.stream()
                .sorted(Comparator.comparing(Product::name).reversed())
                .toList();

        when(repository.findAll(any(Sort.class))).thenReturn(dbData);

        List<ProductDto> actualArray = service.getAll(DESC, "name");
        expectedArray = expectedArray.stream()
                .sorted(Comparator.comparing(ProductDto::name).reversed())
                .toList();
        assertThat(actualArray).isEqualTo(expectedArray);
    }

    @Test
    void process_shouldReturnSortedProducts_whenGetAllByAscPrice() {
        List<Product> dbData = new ArrayList<>();
        List<ProductDto> expectedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = Faker.instance().name().firstName().repeat(2);
            Double price = Faker.instance().number().randomDouble(2, 1, 100);
            LocalDateTime now = LocalDateTime.now();
            UUID uuid = UUID.randomUUID();
            ProductDto productDto = new ProductDto(uuid, name, price, null, null, now, null, null);
            Product product = new Product(uuid, name, price, null, null, now, null, null);

            dbData.add(product);
            expectedArray.add(productDto);
        }
        dbData = dbData.stream()
                .sorted(Comparator.comparing(Product::price))
                .toList();

        when(repository.findAll(any(Sort.class))).thenReturn(dbData);

        List<ProductDto> actualArray = service.getAll(ASC, "price");
        expectedArray = expectedArray.stream()
                .sorted(Comparator.comparing(ProductDto::price))
                .toList();
        assertThat(actualArray).isEqualTo(expectedArray);
    }

    @Test
    void process_shouldDeleteProduct_whenDeleteProduct() {
        UUID uuid = UUID.randomUUID();
        Product product = Product.builder()
                .uuid(uuid)
                .build();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        doNothing().when(repository).deleteById(any(UUID.class));

        ProductDto actualDto = service.delete(uuid);
        ProductDto expectedDto = productMapper.toDto(product);

        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    void process_shouldThrowBadRequestException_whenTryToGetProductByIdWitchDoesntExist() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.delete(UUID.randomUUID()))
                .isInstanceOf(BadRequestException.class);
    }
}