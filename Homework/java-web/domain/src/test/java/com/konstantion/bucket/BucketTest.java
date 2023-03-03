package com.konstantion.bucket;

import com.konstantion.product.Product;
import com.konstantion.user.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class BucketTest {

    @Test
    void process_shouldAddProductsToBucket_whenBucketAddProduct() {
        Bucket bucket = new Bucket();

        Product bread = Product.builder()
                .name("Bread")
                .price(25.0)
                .build();

        Product tea = Product.builder()
                .name("tea")
                .price(25.0)
                .build();

        bucket.addProduct(bread);
        bucket.addProduct(bread);
        bucket.addProduct(tea);

        assertThat(bucket.products())
                .containsEntry(bread, 2)
                .containsEntry(tea, 1);
    }

    @Test
    void process_shouldReturnTotalPrice_whenBucketGetTotalPrice() {
        Bucket bucket = new Bucket();

        Product bread = Product.builder()
                .name("Bread")
                .price(25.0)
                .build();

        Product tea = Product.builder()
                .name("tea")
                .price(25.0)
                .build();

        bucket.addProduct(bread);
        bucket.addProduct(bread);
        bucket.addProduct(tea);

        assertThat(bucket.getTotalPrice())
                .isEqualTo(75);
    }

    @Test
    void process_shouldRemoveProductFromBucket_whenBucketRemoveProduct() {
        Bucket bucket = new Bucket();

        Product bread = Product.builder()
                .name("Bread")
                .price(25.0)
                .build();

        Product tea = Product.builder()
                .name("tea")
                .price(25.0)
                .build();

        bucket.addProduct(bread);
        bucket.addProduct(bread);
        bucket.addProduct(tea);
        bucket.removeProduct(bread);

        assertThat(bucket.products())
                .containsEntry(bread, 1)
                .containsEntry(tea, 1);
    }

    @Test
    void process_shouldDoNothing_whenBucketRemoveProduct_thatDoesntExist() {
        Bucket bucket = new Bucket();

        Product bread = Product.builder()
                .name("Bread")
                .price(25.0)
                .build();

        bucket.removeProduct(bread);

        assertThat(bucket.products())
                .isEmpty();
    }

    @Test
    @DisplayName("When delete more product than bucket has product should be removed")
    void process_shouldRemoveProduct_whenBucketRemoveProduct() {
        Bucket bucket = new Bucket();

        Product bread = Product.builder()
                .name("Bread")
                .price(25.0)
                .build();


        bucket.addProduct(bread);
        bucket.addProduct(bread);
        bucket.removeProduct(bread);
        bucket.removeProduct(bread);
        bucket.removeProduct(bread);

        assertThat(bucket.products())
                .isEmpty();
    }

    @Test
    @Disabled
    void process_returnBucketWithUser_whenSetUser() {
        Bucket bucket = new Bucket();
        User user = new User(1L, UUID.randomUUID(), "name", "email", "123");

//        bucket = bucket.setUser(user);
//
//        assertThat(bucket.user()).isEqualTo(user);
    }
}