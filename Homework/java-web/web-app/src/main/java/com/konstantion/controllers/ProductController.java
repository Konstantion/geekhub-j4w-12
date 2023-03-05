package com.konstantion.controllers;


import com.konstantion.bucket.Bucket;
import com.konstantion.bucket.BucketService;
import com.konstantion.product.ProductService;
import com.konstantion.product.dto.CreationProductDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("products")
public record ProductController(ProductService productService,
                                BucketService bucketService,
                                Bucket bucket
) {
    @GetMapping
    public String getProducts(Model model) {
        model.addAttribute("products", productService.getAllWithReview());
        return "product/products-list";
    }

    @PostMapping
    public String addProduct(
            @ModelAttribute CreationProductDto productCreationDto,
            @RequestParam("file") MultipartFile file
    ) {
        productService.create(productCreationDto, file);
        return "redirect:/products";
    }
}
