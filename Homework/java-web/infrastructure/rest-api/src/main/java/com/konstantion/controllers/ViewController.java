package com.konstantion.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public record ViewController() {
    @GetMapping("/bucket")
    public String getBucketView() {
        return "forward:/bucket.html";
    }

    @GetMapping()
    public String getProductView() {
        return "forward:/index.html";
    }
}
