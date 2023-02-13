package controller;

import org.springframework.data.domain.Sort;
import product.ProductService;
import product.dto.CreationProductDto;
import product.dto.ProductDto;

import java.util.List;

public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public ProductDto addProduct(CreationProductDto creationProductDto) {
        return productService.create(creationProductDto);
    }

    public List<ProductDto> getProducts(Sort.Direction direction, String parameter) {
        return productService.getAll(direction, parameter);
    }

    public ProductDto deleteProduct(Long id) {
        return productService.delete(id);
    }
}
