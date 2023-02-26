package com.konstantion.controller;

import com.konstantion.product.ProductService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public record CliProductController(ProductService productService) {

    public ProductDto addProduct(CreationProductDto creationProductDto) {
        return productService.create(creationProductDto);
    }

    public List<ProductDto> getProducts(Sort.Direction direction, String parameter) {
        return productService.getAll(direction, parameter);
    }

    public ProductDto deleteProduct(UUID uuid) {
        return productService.delete(uuid);
    }
}
