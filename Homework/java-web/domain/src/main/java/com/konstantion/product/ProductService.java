package com.konstantion.product;

import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.product.dto.UpdateProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto create(CreationProductDto createProductDto, MultipartFile file);

    ProductDto getById(UUID uuid);

    ProductDto delete(UUID uuid);

    ProductDto update(UUID uuid, UpdateProductDto productDto);

    List<ProductDto> getAll();

    List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName);

    List<ProductDto> getAll(Sort.Direction sortOrder, String fieldName, String namePattern);

    Page<ProductDto> getAll(
            Integer pageNumber,
            Integer pageSize,
            String fieldName,
            String namePattern,
            UUID categoryUuid,
            boolean ascending
    );

    default ProductDto delete(Product product) {
        return delete(product.uuid());
    }

    String getProductImageEncoded(UUID uuid);

    byte[] getProductImageBytes(UUID uuid);
}
