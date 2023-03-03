package com.konstantion.Controllers;


import com.konstantion.product.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("products")
public record ProductController(ProductService productService) {

    @GetMapping
    public String getProducts(Model model) {
        model.addAttribute("products", productService.getAllWithReview());

        return "product/products-list";
    }
}
