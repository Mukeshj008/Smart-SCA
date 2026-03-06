package com.example.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration.
 * Uses: spring-core
 */
@Configuration
@ComponentScan(basePackages = "com.example.app")
public class AppConfig {
}
