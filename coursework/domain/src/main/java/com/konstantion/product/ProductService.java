package com.konstantion.product;

import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.user.User;

import java.util.UUID;

public interface ProductService {
    ProductDto create(CreationProductDto cpdto, User user);

    /**
     * This method isn't safe and delete entity in DB,
     * witch can lead to the destruction of relationships with other entities,
     * if you want to safely disable entity use {@link #deactivate(UUID, User)} instead.
     */
    ProductDto delete(UUID productId, User user);
    ProductDto deactivate(UUID productId, User user);
    ProductDto activate(UUID productId, User user);
    ProductDto getById(UUID productId);
}
