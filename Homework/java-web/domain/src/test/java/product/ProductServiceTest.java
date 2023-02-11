package product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import product.dto.CreationProductDto;
import product.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest {
    private ProductRepository repository;
    private ProductService service;

    @BeforeEach
    void setUp() {
        repository = new ProductRepository();
        service = new ProductService(repository);
    }

    @Test
    void process_shouldReturnProductId_whenCreateProduct() {
        CreationProductDto creationProductDto = new CreationProductDto();
        creationProductDto.setName("Bread");
        creationProductDto.setPrice(1);

        ProductDto productDto = service.create(creationProductDto);

        assertThat(productDto.getId()).isEqualTo(1L);
    }

}