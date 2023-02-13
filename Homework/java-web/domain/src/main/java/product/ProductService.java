package product;

import exceptions.BadRequestException;
import exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import product.dto.CreationProductDto;
import product.dto.ProductDto;
import product.validator.ProductValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.data.domain.Sort.Direction.ASC;

public class ProductService {
    private final ProductValidator productValidator;
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static final ProductMapper MAPPER = ProductMapper.INSTANCE;

    public ProductService(ProductValidator productValidator, ProductRepository productRepository) {
        this.productValidator = productValidator;
        this.productRepository = productRepository;
    }

    public ProductDto create(CreationProductDto createProductDto) {
        Optional<Map<String, String>> validationResult = productValidator
                .validate(createProductDto);

        if (validationResult.isPresent()) {
            logger.warn("Failed to create product {}, given data is invalid {}",
                    createProductDto, validationResult.get());
            throw new ValidationException("Failed to create product, given data is invalid",
                    validationResult.get());
        }

        Product product = MAPPER.toEntity(createProductDto);

        product.setCreatedAt(LocalDateTime.now());

        product = productRepository.saveAndFlush(product);

        ProductDto productDto = MAPPER.toDto(product);

        logger.info("Product {} successfully created", productDto);

        return productDto;
    }

    public ProductDto delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new BadRequestException(format("Product with id %s doesn't exist", id)
                ));

        productRepository.deleteById(id);

        logger.info("Product with id {} successfully delete", id);
        return MAPPER.toDto(product);
    }

    public List<ProductDto> getAll() {
        return getAll(ASC, "id");
    }

    public List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName) {
        List<Product> products = productRepository.findAll(Sort.by(sortOrder, fieldName));

        return MAPPER.toDto(products);
    }
}
