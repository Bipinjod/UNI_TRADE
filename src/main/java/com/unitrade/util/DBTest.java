package com.unitrade.util;

/**
 * DBTest - Simple standalone utility to verify database connectivity.
 *
 * Run this class directly (it has a main method) to test whether
 * the database.properties config is correct and MySQL is reachable.
 *
 * Usage (from IntelliJ):
 *   Right-click this file → Run 'DBTest.main()'
 *
 * Usage (from terminal):
 *   mvn compile exec:java -Dexec.mainClass="com.unitrade.util.DBTest"
 */
public class DBTest {

    public static void main(String[] args) {
        System.out.println("=== UniTrade Database Connection Test ===");
        System.out.println();

        try {
            boolean connected = DBConnection.testConnection();

            System.out.println();
            if (connected) {
                System.out.println("SUCCESS: Database connection established successfully!");
                System.out.println("The application is ready to connect to MySQL.");
            } else {
                System.out.println("FAILURE: Could not establish database connection.");
                System.out.println("Check your database.properties file and MySQL server status.");
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println("ERROR: " + e.getMessage());
            System.out.println();
            System.out.println("Troubleshooting:");
            System.out.println("  1. Is MySQL running? (check via 'mysqladmin ping' or MySQL Workbench)");
            System.out.println("  2. Does the database 'unitrade_db' exist? (run unitrade_final_schema.sql)");
            System.out.println("  3. Are credentials correct in src/main/resources/database.properties?");
            System.out.println("  4. Is mysql-connector-j in pom.xml and downloaded?");
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("=== Test Complete ===");
    }
}

