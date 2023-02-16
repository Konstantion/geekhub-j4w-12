package product;

import com.github.javafaker.Faker;
import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.product.ProductRepository;
import com.konstantion.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.validator.ProductValidations;
import com.konstantion.product.validator.ProductValidator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

class ProductServiceTest {
    private ProductRepository repository;
    private ProductValidations creationProductDtoValidator;
    private ProductValidator validator;
    private ProductService service;

    @BeforeEach
    void setUp() {
        creationProductDtoValidator = new ProductValidations();
        validator = new ProductValidator(creationProductDtoValidator);
        repository = new ProductRepository();
        service = new ProductService(validator, repository);
    }

    @Test
    void process_shouldReturnProductId_whenCreateProduct() {
        CreationProductDto creationProductDto = new CreationProductDto();
        creationProductDto.setName("Bread");
        creationProductDto.setPrice(1);

        ProductDto firstProductDto = service.create(creationProductDto);
        ProductDto secondProductDto = service.create(creationProductDto);

        assertThat(firstProductDto.getId()).isEqualTo(1L);
        assertThat(secondProductDto.getId()).isEqualTo(2L);
    }

    @Test
    void process_shouldThrowValidationException_whenProductIsInvalid() {
        CreationProductDto creationProductDto = new CreationProductDto();
        creationProductDto.setName("");
        creationProductDto.setPrice(1);

        assertThatThrownBy(() -> service.create(creationProductDto))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void process_shouldReturnSortedProducts_whenGetAllByDescName() {
        List<ProductDto> expectedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CreationProductDto creationProductDto = new CreationProductDto();
            creationProductDto.setName(Faker.instance().name().firstName().repeat(2));
            creationProductDto.setPrice(Faker.instance().number().numberBetween(1, 100));
            expectedArray.add(service.create(creationProductDto));
        }

        List<ProductDto> actualArray = service.getAll(DESC, "name");
        expectedArray = expectedArray.stream()
                .sorted(Comparator.comparing(ProductDto::getName).reversed())
                .toList();
        assertThat(actualArray).isEqualTo(expectedArray);
    }

    @Test
    void process_shouldReturnSortedProducts_whenGetAllByAscPrice() {
        List<ProductDto> expectedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CreationProductDto creationProductDto = new CreationProductDto();
            creationProductDto.setName(Faker.instance().name().firstName().repeat(2));
            creationProductDto.setPrice(Faker.instance().number().numberBetween(1, 100));
            expectedArray.add(service.create(creationProductDto));
        }

        List<ProductDto> actualArray = service.getAll(ASC, "price");
        expectedArray = expectedArray.stream()
                .sorted(Comparator.comparing(ProductDto::getPrice))
                .toList();
        assertThat(actualArray).isEqualTo(expectedArray);
    }

    @Test
    void process_shouldReturnSortedProductsById_whenGetAll() {
        List<ProductDto> expectedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CreationProductDto creationProductDto = new CreationProductDto();
            creationProductDto.setName(Faker.instance().name().firstName().repeat(2));
            creationProductDto.setPrice(Faker.instance().number().numberBetween(1, 100));
            expectedArray.add(service.create(creationProductDto));
        }

        List<ProductDto> actualArray = service.getAll();
        expectedArray = expectedArray.stream()
                .sorted(Comparator.comparing(ProductDto::getId))
                .toList();
        assertThat(actualArray).isEqualTo(expectedArray);
    }

    @Test
    void process_shouldDeleteProduct_whenDeleteProduct() {
        List<ProductDto> expectedArray = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CreationProductDto creationProductDto = new CreationProductDto();
            creationProductDto.setName(Faker.instance().name().firstName().repeat(2));
            creationProductDto.setPrice(Faker.instance().number().numberBetween(1, 100));
            expectedArray.add(service.create(creationProductDto));
        }
        ProductDto productWithId1 = service.delete(1L);
        ProductDto productWithId6 = service.delete(6L);
        List<ProductDto> actualArray = service.getAll();

        expectedArray.removeIf(p -> p.getId().equals(1L) || p.getId().equals(6L));
        assertThat(actualArray).isEqualTo(expectedArray);
        assertThat(productWithId1.getId()).isEqualTo(1L);
        assertThat(productWithId6.getId()).isEqualTo(6L);
    }

    @Test
    void process_shouldThrowBadRequestException_whenTryToGetProductByIdWitchDoesntExist() {
        CreationProductDto creationProductDto = new CreationProductDto();
        creationProductDto.setName("Name");
        creationProductDto.setPrice(1);
        service.create(creationProductDto);
        assertThatThrownBy(() -> service.delete(10L))
                .isInstanceOf(BadRequestException.class);
    }
}