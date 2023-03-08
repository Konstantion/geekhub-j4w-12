package com.konstantion.product;

import com.konstantion.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class Product {
    private UUID uuid;
    private String name;
    private Double price;
    private Double weight;
    private String description;
    private String imagePath;
    private User creator;
    private LocalDateTime createdAt;
}
