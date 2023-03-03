package com.konstantion.product;

import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductService {
    ProductDto create(CreationProductDto createProductDto, MultipartFile multipartFile);

    ProductDto delete(UUID uuid);

    List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName);

    List<ProductDto> getAll();

    List<Map.Entry<ProductDto, Double>> getAllWithRating();

    List<ProductDto> getAllWithReview();

    default ProductDto delete(Product product) {
        return delete(product.uuid());
    }
}
