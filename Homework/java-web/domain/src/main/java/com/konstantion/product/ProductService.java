package com.konstantion.product;

import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductService {
    ProductDto create(CreationProductDto createProductDto);

    ProductDto delete(UUID uuid);

    List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName);

    List<ProductDto> getAll();

    List<Map.Entry<ProductDto, Double>> getAllWithRating();

    default ProductDto delete(Product product) {
        return delete(product.uuid());
    }
}
