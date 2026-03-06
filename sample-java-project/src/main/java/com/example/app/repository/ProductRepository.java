package com.example.app.repository;

import com.example.app.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory product repository.
 * Uses: commons-lang3, log4j
 */
public class ProductRepository {
    private static final Logger logger = LogManager.getLogger(ProductRepository.class);
    private final Map<String, Product> products = new ConcurrentHashMap<>();

    public Product save(Product product) {
        if (product != null && StringUtils.isNotBlank(product.getId())) {
            products.put(product.getId(), product);
            logger.info("Saved product: {}", product.getId());
        }
        return product;
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    public List<Product> searchByName(String name) {
        return products.values().stream()
                .filter(p -> StringUtils.containsIgnoreCase(p.getName(), name))
                .collect(java.util.stream.Collectors.toList());
    }
}
