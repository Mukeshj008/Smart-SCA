package com.example.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main application entry point.
 * Uses: log4j, spring-core/context
 */
public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Starting Sample Vulnerable Application");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        logger.info("Application context initialized");
    }
}
