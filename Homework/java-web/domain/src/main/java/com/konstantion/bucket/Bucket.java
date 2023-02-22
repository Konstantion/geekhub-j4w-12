package com.konstantion.bucket;

import com.konstantion.product.Product;
import com.konstantion.user.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public record Bucket(User user, Map<Product, Integer> products) {

    public Bucket() {
        this(null, Collections.emptyMap());
    }

    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            products.computeIfPresent(product, (k, v) -> ++v);
        } else {
            products.putIfAbsent(product, 1);
        }
    }

    public Integer getTotalPrice() {
        return products
                .entrySet()
                .stream()
                .mapToInt(entry -> entry.getKey().price() * entry.getValue())
                .reduce(0, Integer::sum);
    }

    public Bucket setUser(User user) {
        return new Bucket(user, products);
    }
}
