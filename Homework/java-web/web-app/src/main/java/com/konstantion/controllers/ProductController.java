package com.konstantion.controllers;


import com.konstantion.bucket.Bucket;
import com.konstantion.bucket.BucketService;
import com.konstantion.exceptions.FileIOException;
import com.konstantion.product.ProductService;
import com.konstantion.product.dto.CreationProductDto;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("products")
public record ProductController(ProductService productService,
                                BucketService bucketService,
                                Bucket bucket
) {
    @GetMapping("/list")
    public ResponseEntity<Response> getProducts(
            @RequestParam Optional<String> parameter,
            @RequestParam Optional<String> pattern,
            Model model) {

        if(pattern.orElse("").equals("error")) {
            throw new FileIOException("What the hell just happened");
        }
        List<ProductDto> products = productService.getAll(
                DESC,
                parameter.orElse("name").toLowerCase(),
                pattern.orElse("").toLowerCase());


        return ResponseEntity.ok(Response.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .status(OK)
                .data(Map.of("products", products))
                .build()
        );
    }

    @GetMapping()
    public String getView() {
        return "products-list";
    }

    @PostMapping
    public ResponseEntity<Response> addProduct(
            @ModelAttribute CreationProductDto productCreationDto,
            @RequestParam("file") MultipartFile file
    ) {
        ProductDto dto = productService.create(productCreationDto, file);
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(OK.value())
                        .status(OK)
                        .data(Map.of("product", dto))
                        .build()
        );
    }

    @DeleteMapping("/{uuid}")
    public String deleteProduct(@PathVariable UUID uuid) {
        productService.delete(uuid);
        return "redirect:/products";
    }
}
