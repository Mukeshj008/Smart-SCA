package com.example.app.controller;

import com.example.app.model.Order;
import com.example.app.service.JsonService;
import com.example.app.service.ValidationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Set;

/**
 * REST controller for Order operations.
 * Uses: jackson, hibernate-validator, log4j, spring
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger logger = LogManager.getLogger(OrderController.class);
    private final JsonService jsonService = new JsonService();
    private final ValidationService validationService = new ValidationService();

    @PostMapping
    public String createOrder(@RequestBody String body) {
        logger.info("Creating order from JSON");
        Order order = jsonService.fromJson(body, Order.class);
        if (order == null) return "{\"error\":\"Invalid JSON\"}";
        Set<String> errors = validationService.validate(order);
        if (!errors.isEmpty()) return "{\"errors\":" + jsonService.toJson(errors) + "}";
        return jsonService.toJson(order);
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id) {
        logger.debug("Fetching order {}", id);
        Order order = new Order(id, "PROD-001", 2, new BigDecimal("199.98"));
        return jsonService.toJson(order);
    }
}
