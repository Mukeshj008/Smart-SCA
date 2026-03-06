package com.example.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson ObjectMapper configuration.
 * Uses: jackson-databind, log4j
 */
@Configuration
public class JacksonConfig {
    private static final Logger logger = LogManager.getLogger(JacksonConfig.class);

    @Bean
    public ObjectMapper objectMapper() {
        logger.debug("Configuring ObjectMapper");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }
}
