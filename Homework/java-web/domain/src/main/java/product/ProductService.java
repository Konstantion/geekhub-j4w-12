package product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import product.dto.CreationProductDto;
import product.dto.ProductDto;

import java.time.LocalDateTime;

public class ProductService {
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static final ProductMapper MAPPER = ProductMapper.INSTANCE;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDto create(CreationProductDto createProductDto) {
        Product product = MAPPER.toEntity(createProductDto);

        product.setCreatedAt(LocalDateTime.now());

        productRepository.save(product);

        ProductDto productDto = MAPPER.toDto(product);

        logger.info("Product {} successfully created", productDto);

        return productDto;
    }
}
