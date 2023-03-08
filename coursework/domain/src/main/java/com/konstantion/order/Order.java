package com.konstantion.order;

import com.konstantion.product.Product;
import com.konstantion.table.Table;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class Order {
    private UUID uuid;
    private Map<Product, Integer> products;
    private Table table;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private Boolean active;
}
