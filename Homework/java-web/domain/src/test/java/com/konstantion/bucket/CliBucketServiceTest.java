package com.konstantion.bucket;

import com.konstantion.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CliBucketServiceTest {
    @Mock
    private Bucket bucket;

    private CliBucketService bucketService;

    @BeforeEach
    void setUp() {
        bucketService = new CliBucketService();
    }

    @Test
    void process_shouldAddProductToBucket_whenAddProductToBucket() {
        doNothing().when(bucket).addProduct(any(Product.class));

        Product product = Product.builder()
                .name("Bread")
                .price(25)
                .build();

        bucketService.addProductToBucket(bucket, product);

        verify(bucket, times(1)).addProduct(any(Product.class));
    }

    @Test
    void process_shouldAddProductsToBucket_whenAddProductsToBucket() {
        doNothing().when(bucket).addProduct(any(Product.class));

        Product bread = Product.builder()
                .name("Bread")
                .price(25)
                .build();

        Product bear = Product.builder()
                .name("Bear")
                .price(40)
                .build();

        bucketService.addProductsToBucket(bucket, List.of(bread, bear));

        verify(bucket, times(2)).addProduct(any(Product.class));
    }

    @Test
    void process_shouldRemoveProduct_whenRemoveProductFromBucket() {
        doReturn(true).when(bucket).removeProduct(any(Product.class));

        Product bear = Product.builder()
                .name("Bear")
                .price(40)
                .build();

        bucketService.removeProductFromBucket(bucket, bear);

        verify(bucket, times(1)).removeProduct(any(Product.class));
    }

    @Test
    void process_shouldAddProduct_whenAddProductCountToBucket() {
        doNothing().when(bucket).addProduct(any(Product.class));

        Product bear = Product.builder()
                .name("Bear")
                .price(40)
                .build();
        bucketService.addProductCountToBucket(bucket, bear, 10);

        verify(bucket, times(10)).addProduct(any(Product.class));
    }

}