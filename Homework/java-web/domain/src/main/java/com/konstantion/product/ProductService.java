package com.konstantion.product;

import com.konstantion.product.model.CreationProductRequest;
import com.konstantion.product.model.UpdateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product create(CreationProductRequest createProductDto, MultipartFile file);

    Product getById(UUID uuid);

    Product delete(UUID uuid);

    Product update(UUID uuid, UpdateProductRequest productDto);

    List<Product> getAll();

    List<Product> getAll(Sort.Direction sortOrder, String fieldName);

    List<Product> getAll(Sort.Direction sortOrder, String fieldName, String namePattern);

    Page<Product> getAll(
            Integer pageNumber,
            Integer pageSize,
            String fieldName,
            String namePattern,
            UUID categoryUuid,
            boolean ascending
    );

    default Product delete(Product product) {
        return delete(product.uuid());
    }

    String getProductImageEncoded(UUID uuid);

    byte[] getProductImageBytes(UUID uuid);
}
