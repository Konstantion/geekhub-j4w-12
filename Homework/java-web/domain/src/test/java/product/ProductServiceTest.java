package product;

import com.github.javafaker.Faker;
import exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import product.dto.CreationProductDto;
import product.dto.ProductDto;
import product.validator.ProductValidations;
import product.validator.ProductValidator;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
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
    void process_shouldThrowError_whenProductIsInvalid() {
        CreationProductDto creationProductDto = new CreationProductDto();
        creationProductDto.setName("");
        creationProductDto.setPrice(1);

        assertThatThrownBy(() -> service.create(creationProductDto))
                .isInstanceOf(ValidationException.class);

    }

    @Test
    void process_shouldReturnSortedProducts_whenGetAll() {
        for(int i = 0; i < 10; i++) {
            CreationProductDto creationProductDto = new CreationProductDto();
            creationProductDto.setName(Faker.instance().name().firstName());
            creationProductDto.setPrice(Faker.instance().number().numberBetween(1, 100));
            service.create(creationProductDto);
        }

        List<ProductDto> productDtoList = service.getAll(DESC, "name");
        System.out.println(productDtoList);
    }

}