package com.unitrade.util;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application lifecycle listener that cleanly shuts down MySQL resources
 * to prevent memory leaks when the web application is stopped or redeployed.
 *
 * Fixes:
 *  - "registered the JDBC driver but failed to unregister it" WARNING
 *  - "started a thread named [mysql-cj-abandoned-connection-cleanup] but has
 *    failed to stop it" WARNING
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("UniTrade application started.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("UniTrade application stopping — cleaning up JDBC resources...");

        // 1. Shut down MySQL's AbandonedConnectionCleanupThread
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
            LOGGER.info("MySQL AbandonedConnectionCleanupThread shut down successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not shut down AbandonedConnectionCleanupThread", e);
        }

        // 2. Deregister all JDBC drivers registered by this web app's ClassLoader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                try {
                    DriverManager.deregisterDriver(driver);
                    LOGGER.info("Deregistered JDBC driver: " + driver);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to deregister JDBC driver: " + driver, e);
                }
            }
        }

        LOGGER.info("UniTrade application cleanup complete.");
    }
}

