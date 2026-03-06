package com.example.app.facade;

import com.example.app.config.ElasticsearchConfig;
import com.example.app.model.Order;
import com.example.app.model.Product;
import com.example.app.model.User;
import com.example.app.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Main application facade orchestrating all services.
 * Uses: jackson, gson, log4j, elasticsearch, hibernate-validator, commons-beanutils
 */
public class ApplicationFacade {
    private static final Logger logger = LogManager.getLogger(ApplicationFacade.class);

    private final JsonService jsonService = new JsonService();
    private final GsonService gsonService = new GsonService();
    private final BeanCopyService beanCopyService = new BeanCopyService();
    private final ValidationService validationService = new ValidationService();

    public String processUserJson(String json) {
        logger.info("Processing user JSON");
        User user = jsonService.fromJson(json, User.class);
        if (user == null) return "{}";
        Set<String> errors = validationService.validate(user);
        if (!errors.isEmpty()) return jsonService.toJson(errors);
        return jsonService.toJson(user);
    }

    public String processProductJson(String json) {
        logger.info("Processing product JSON via Gson");
        Product product = gsonService.fromJson(json, Product.class);
        return gsonService.toJson(product);
    }

    public Order copyOrder(Order source) {
        Order dest = new Order();
        beanCopyService.copyProperties(dest, source);
        return dest;
    }

    public List<?> searchElasticsearch(String index, String field, String value) {
        try {
            ElasticsearchConfig config = new ElasticsearchConfig();
            TransportClient client = config.createClient();
            SearchService searchService = new SearchService(client);
            return searchService.search(index, field, value);
        } catch (Exception e) {
            logger.error("Elasticsearch search failed", e);
            return Collections.emptyList();
        }
    }
}
