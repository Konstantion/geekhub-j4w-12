package com.konstantion.bucket;

import com.konstantion.product.Product;
import com.konstantion.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CliBucketServiceTest {
    @Mock
    private Bucket bucket;

    private CliBucketService bucketService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        bucketService = new CliBucketService(productRepository);
    }

    @Test
    void process_shouldAddProductToBucket_whenAddProductToBucket() {
        UUID uuid = UUID.randomUUID();
        doNothing().when(bucket).addProduct(any(Product.class));
        doReturn(Optional.of(Product.builder().build())).when(productRepository).findByUuid(any(UUID.class));

        bucketService.addProductToBucket(bucket, uuid);

        verify(bucket, times(1)).addProduct(any(Product.class));
    }

    @Test
    void process_shouldAddProductsToBucket_whenAddProductsToBucket() {
        doNothing().when(bucket).addProduct(any(Product.class));

        Product bread = Product.builder()
                .name("Bread")
                .price(25.0)
                .build();

        Product bear = Product.builder()
                .name("Bear")
                .price(40.0)
                .build();

        bucketService.addProductsToBucket(bucket, List.of(bread, bear));

        verify(bucket, times(2)).addProduct(any(Product.class));
    }

    @Test
    void process_shouldRemoveProduct_whenRemoveProductFromBucket() {
        UUID uuid = UUID.randomUUID();
        Product product = Product.builder()
                .uuid(uuid)
                .name("Bread")
                .price(25.0)
                .build();

        doReturn(true).when(bucket).removeProduct(any(Product.class));
        doReturn(Optional.of(product)).when(productRepository).findByUuid(any(UUID.class));

        bucketService.removeProductFromBucket(bucket, uuid);

        verify(bucket, times(1)).removeProduct(any(Product.class));
    }

    @Test
    void process_shouldAddProduct_whenAddProductCountToBucket() {
        UUID uuid = UUID.randomUUID();
        Product bear = Product.builder()
                .uuid(uuid)
                .name("Bear")
                .price(40.0)
                .build();

        doNothing().when(bucket).addProduct(any(Product.class));
        doReturn(Optional.of(bear)).when(productRepository).findByUuid(any(UUID.class));

        bucketService.addProductQuantityToBucket(bucket, uuid, 10);

        verify(bucket, times(10)).addProduct(any(Product.class));
    }

}