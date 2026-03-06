package com.example.app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Alternative JSON handling via Gson.
 * Uses: gson, log4j
 */
public class GsonService {
    private static final Logger logger = LogManager.getLogger(GsonService.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public <T> String toJson(T obj) {
        logger.debug("Serializing object to JSON via Gson");
        return gson.toJson(obj);
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        logger.debug("Deserializing JSON via Gson");
        return gson.fromJson(json, clazz);
    }
}
