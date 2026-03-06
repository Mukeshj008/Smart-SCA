package com.example.app.controller;

import com.example.app.model.Product;
import com.example.app.service.GsonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Product operations.
 * Uses: gson, log4j, spring
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger logger = LogManager.getLogger(ProductController.class);
    private final GsonService gsonService = new GsonService();

    @GetMapping("/{id}")
    public String getProduct(@PathVariable String id) {
        logger.info("Fetching product {}", id);
        Product product = new Product(id, "Sample Product", 99.99);
        return gsonService.toJson(product);
    }

    @PostMapping
    public String createProduct(@RequestBody String body) {
        logger.info("Creating product from JSON");
        Product product = gsonService.fromJson(body, Product.class);
        return gsonService.toJson(product);
    }
}
