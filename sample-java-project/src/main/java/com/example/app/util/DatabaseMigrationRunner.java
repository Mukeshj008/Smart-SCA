package com.example.app.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database migration runner stub.
 * Liquibase is a declared dependency but NOT imported/used here - not reachable in code.
 * Uses: log4j only
 */
public class DatabaseMigrationRunner {
    private static final Logger logger = LogManager.getLogger(DatabaseMigrationRunner.class);

    public void runMigrations(String jdbcUrl, String user, String password) throws SQLException {
        logger.info("Database migration placeholder - liquibase-core declared in pom but not reachable");
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password)) {
            // No liquibase import - dependency present in pom but not used in any source file
        }
    }
}
