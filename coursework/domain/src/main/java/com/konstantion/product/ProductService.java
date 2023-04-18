package com.konstantion.product;

import com.konstantion.product.model.CreateProductRequest;
import com.konstantion.product.model.GetProductsRequest;
import com.konstantion.user.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProductService {
    Product create(CreateProductRequest createProductRequest, User user);

    Page<Product> getAll(GetProductsRequest getProductsRequest, boolean onlyActive);

    default Page<Product> getAll(GetProductsRequest getProductsRequest) {
        return getAll(getProductsRequest, true);
    }

    /**
     * This method isn't safe and delete entity in DB,
     * witch can lead to the destruction of relationships with other entities,
     * if you want to safely disable entity use {@link #deactivate(UUID, User)} instead.
     */
    Product delete(UUID productId, User user);

    Product deactivate(UUID productId, User user);

    Product activate(UUID productId, User user);

    Product getById(UUID productId);
}
