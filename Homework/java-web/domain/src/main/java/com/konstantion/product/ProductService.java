package com.konstantion.product;

import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ProductService {
    ProductDto create(CreationProductDto createProductDto);

    ProductDto delete(Long id);

    List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName);

    List<ProductDto> getAll();
    default ProductDto delete(Product product) {
        return delete(product.id());
    }
}
