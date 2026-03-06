package com.example.app.batch;

import com.example.app.model.Order;
import com.example.app.model.Product;
import com.example.app.model.User;
import com.example.app.service.GsonService;
import com.example.app.service.JsonService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Batch report generation.
 * Uses: jackson, gson, commons-lang3, log4j
 */
public class ReportGenerator {
    private static final Logger logger = LogManager.getLogger(ReportGenerator.class);
    private final JsonService jsonService = new JsonService();
    private final GsonService gsonService = new GsonService();

    public String generateUserReport(List<User> users) {
        logger.info("Generating user report for {} users", users.size());
        return jsonService.toJson(users);
    }

    public String generateProductReport(List<Product> products) {
        logger.info("Generating product report");
        return gsonService.toJson(products);
    }

    public String generateOrderReport(List<Order> orders) {
        logger.info("Generating order report");
        return jsonService.toJson(orders);
    }

    public List<String> extractEmails(List<User> users) {
        List<String> emails = new ArrayList<>();
        for (User u : users) {
            if (StringUtils.isNotBlank(u.getEmail())) {
                emails.add(u.getEmail());
            }
        }
        return emails;
    }
}
