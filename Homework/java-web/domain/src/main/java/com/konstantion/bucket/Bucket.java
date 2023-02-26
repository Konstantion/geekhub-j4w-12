package com.konstantion.bucket;

import com.konstantion.product.Product;
import com.konstantion.user.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public record Bucket(User user, Map<Product, Integer> products) {

    public Bucket() {
        this(null, new HashMap<>());
    }

    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            products.computeIfPresent(product, (k, v) -> ++v);
        } else {
            products.put(product, 1);
        }
    }

    public boolean removeProduct(Product product) {
        int productCount = products.getOrDefault(product, 0);
        if (productCount > 1) {
            products.computeIfPresent(product, (k, v) -> --v);
        } else if (productCount == 1) {
            products.remove(product);
        } else {
            return false;
        }

        return true;
    }

    public Double getTotalPrice() {
        return products
                .entrySet()
                .stream()
                .mapToDouble(entry -> entry.getKey().price() * entry.getValue())
                .reduce(0, Double::sum);
    }

    public void clear() {
        products.clear();
    }

    public Bucket setUser(User user) {
        return new Bucket(user, products);
    }
}
