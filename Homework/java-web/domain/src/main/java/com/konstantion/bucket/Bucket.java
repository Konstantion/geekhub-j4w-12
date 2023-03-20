package com.konstantion.bucket;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SessionScope
@Component
public class Bucket {

    private final Map<UUID, Integer> products;

    public Bucket() {
        this(new HashMap<>());
    }

    public Bucket(Map<UUID, Integer> products) {
        this.products = products;
    }

    public Map<UUID, Integer> products() {
        return products;
    }

    public void addProduct(UUID product) {
        if (products.containsKey(product)) {
            products.computeIfPresent(product, (k, v) -> ++v);
        } else {
            products.put(product, 1);
        }
    }

    public boolean removeProduct(UUID product) {
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

    public void setProductQuantity(UUID product, Integer quantity) {
        products.put(product, quantity);
    }

    public void clear() {
        products.clear();
    }

    public int count() {
        return products.size();
    }
}
