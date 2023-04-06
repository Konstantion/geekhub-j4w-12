package com.konstantion.order;

import com.konstantion.product.Product;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {
    @Test
    void arraytest() {
        Product product = Product.builder().build();
        System.out.println(product.getClass().getSimpleName());
    }
}