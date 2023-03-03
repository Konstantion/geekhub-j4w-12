package com.konstantion.bucket;

import com.konstantion.product.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@SessionScope
@Component
public class Bucket {

    private final Map<Product, Integer> products;

    public Bucket() {
        this(new HashMap<>());
    }

    public Bucket(Map<Product, Integer> products) {
        this.products = products;
    }

    public Map<Product, Integer> products() {
        return products;
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

    public int count() {
        return products.size();
    }
}
