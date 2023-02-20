package product;

import com.github.javafaker.Faker;
import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.product.DomainProductService;
import com.konstantion.product.Product;
import com.konstantion.product.ProductRepository;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.validator.ProductValidations;
import com.konstantion.product.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    private DomainProductService service;

    @BeforeEach
    void setUp() {
        productValidations = new ProductValidations();
        validator = new ProductValidator(productValidations);
        repository = mock(ProductRepository.class);
        service = new DomainProductService(validator, repository);
    }

    @Test
    void process_shouldReturnProductId_whenCreateProduct() {
        CreationProductDto creationProductDto = new CreationProductDto("Bread", 1);
        Product productWithIdOne = Product.builder().id(1L).build();
        Product productWithIdTwo = Product.builder().id(2L).build();

        when(repository.saveAndFlush(any(Product.class)))
                .thenReturn(productWithIdOne)
                .thenReturn(productWithIdTwo);

        ProductDto firstProductDto = service.create(creationProductDto);
        ProductDto secondProductDto = service.create(creationProductDto);

        assertThat(firstProductDto.id()).isEqualTo(1L);
        assertThat(secondProductDto.id()).isEqualTo(2L);
    }

    @Test
    void process_shouldThrowValidationException_whenProductIsInvalname() {
        CreationProductDto creationProductDto = new CreationProductDto("", 1);

        assertThatThrownBy(() -> service.create(creationProductDto))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void process_shouldReturnSortedProducts_whenGetAllByDescName() {
        List<Product> dbData = new ArrayList<>();
        List<ProductDto> expectedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = Faker.instance().name().firstName().repeat(2);
            Integer price = Faker.instance().number().numberBetween(1, 100);
            LocalDateTime now = LocalDateTime.now();

            ProductDto productDto = new ProductDto((long) i, name, price, now);
            Product product = new Product((long) i, name, price, now);

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
            Integer price = Faker.instance().number().numberBetween(1, 100);
            LocalDateTime now = LocalDateTime.now();

            ProductDto productDto = new ProductDto((long) i, name, price, now);
            Product product = new Product((long) i, name, price, now);

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
    void process_shouldReturnSortedProductsById_whenGetAll() {
        List<Product> dbData = new ArrayList<>();
        List<ProductDto> expectedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = Faker.instance().name().firstName().repeat(2);
            Integer price = Faker.instance().number().numberBetween(1, 100);
            LocalDateTime now = LocalDateTime.now();

            ProductDto productDto = new ProductDto((long) i, name, price, now);
            Product product = new Product((long) i, name, price, now);

            dbData.add(product);
            expectedArray.add(productDto);
        }
        dbData = dbData.stream()
                .sorted(Comparator.comparing(Product::id))
                .toList();

        when(repository.findAll(any(Sort.class))).thenReturn(dbData);

        List<ProductDto> actualArray = service.getAll();
        expectedArray = expectedArray.stream()
                .sorted(Comparator.comparing(ProductDto::id))
                .toList();
        assertThat(actualArray).isEqualTo(expectedArray);
    }

    @Test
    void process_shouldDeleteProduct_whenDeleteProduct() {
        List<Product> dbData = new ArrayList<>();
        List<ProductDto> expectedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = Faker.instance().name().firstName().repeat(2);
            Integer price = Faker.instance().number().numberBetween(1, 100);
            LocalDateTime now = LocalDateTime.now();

            ProductDto productDto = new ProductDto((long) i, name, price, now);
            Product product = new Product((long) i, name, price, now);

            dbData.add(product);
            expectedArray.add(productDto);
        }
        dbData.removeIf(p -> p.id().equals(1L) || p.id().equals(6L));
        dbData = dbData.stream()
                .sorted(Comparator.comparing(Product::id))
                .toList();

        when(repository.findAll(any(Sort.class))).thenReturn(dbData);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(Product.builder().id(1L).build()));

        service.delete(1L);
        service.delete(6L);
        List<ProductDto> actualArray = service.getAll();

        expectedArray.removeIf(p -> p.id().equals(1L) || p.id().equals(6L));

        assertThat(actualArray).isEqualTo(expectedArray);
        verify(repository, times(2)).deleteById(any(Long.class));
    }

    @Test
    void process_shouldThrowBadRequestException_whenTryToGetProductByIdWitchDoesntExist() {
        CreationProductDto creationProductDto = new CreationProductDto("Name", 1);
        service.create(creationProductDto);
        assertThatThrownBy(() -> service.delete(10L))
                .isInstanceOf(BadRequestException.class);
    }
}