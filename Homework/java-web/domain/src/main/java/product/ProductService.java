package product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import product.dto.ProductDto;

public class ProductService {
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDto save() {
        return null;
    }
}
