package com.example.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * JSON serialization/deserialization service.
 * Uses: jackson-databind, log4j
 */
public class JsonService {
    private static final Logger logger = LogManager.getLogger(JsonService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> String toJson(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            logger.error("Failed to serialize to JSON", e);
            return "{}";
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("Failed to deserialize from JSON", e);
            return null;
        }
    }
}
