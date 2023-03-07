package com.konstantion.product;

import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto create(CreationProductDto createProductDto, MultipartFile multipartFile);

    ProductDto delete(UUID uuid);

    List<ProductDto> getAll();

    List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName);

    List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName, String namePattern);

    default ProductDto delete(Product product) {
        return delete(product.uuid());
    }
}
