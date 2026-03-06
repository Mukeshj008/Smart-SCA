package com.example.app.controller;

import com.example.app.model.User;
import com.example.app.service.JsonService;
import com.example.app.service.ValidationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * REST controller for User operations.
 * Uses: jackson (via JsonService), hibernate-validator, log4j, spring
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final JsonService jsonService = new JsonService();
    private final ValidationService validationService = new ValidationService();

    @PostMapping
    public String createUser(@RequestBody String body) {
        logger.info("Creating user from JSON");
        User user = jsonService.fromJson(body, User.class);
        if (user == null) return "{\"error\":\"Invalid JSON\"}";
        Set<String> errors = validationService.validate(user);
        if (!errors.isEmpty()) return "{\"errors\":" + jsonService.toJson(errors) + "}";
        return jsonService.toJson(user);
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable String id) {
        logger.debug("Fetching user {}", id);
        User user = new User("user" + id, "user" + id + "@example.com");
        return jsonService.toJson(user);
    }
}
