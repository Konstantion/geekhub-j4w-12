package com.konstantion.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web-api")
public class ViewController {
    @GetMapping("/buckets/view")
    public String getBucketView() {
        return "bucket";
    }

    @GetMapping("/products/view")
    public String getProductView() {
        return "products-list";
    }
}
