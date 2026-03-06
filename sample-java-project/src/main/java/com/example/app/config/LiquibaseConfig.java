package com.example.app.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Database migration configuration stub.
 * Liquibase is a declared dependency but NOT imported/used here - not reachable in code.
 * Uses: log4j only
 */
public class LiquibaseConfig {
    private static final Logger logger = LogManager.getLogger(LiquibaseConfig.class);

    public void runMigrations(String jdbcUrl) throws Exception {
        logger.info("Database migration placeholder - liquibase-core is declared but not used");
        try (Connection conn = DriverManager.getConnection(jdbcUrl)) {
            // No liquibase import - dependency present in pom but not reachable
        }
    }
}
