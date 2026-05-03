package com.unitrade.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DBConnection - Database connection utility.
 * Loads config from database.properties on the classpath.
 *
 * Usage:
 *   try (Connection conn = DBConnection.getConnection()) { ... }
 *
 * Configuration file (src/main/resources/database.properties):
 *   db.url=jdbc:mysql://localhost:3306/unitrade_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
 *   db.username=root
 *   db.password=your_password
 *   db.driver=com.mysql.cj.jdbc.Driver
 */
public class DBConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    // Static initializer — runs once when the class is first loaded
    static {
        try {
            Properties props = new Properties();
            InputStream is = DBConnection.class.getClassLoader()
                    .getResourceAsStream("database.properties");

            if (is == null) {
                throw new RuntimeException(
                    "database.properties not found on classpath. " +
                    "Make sure the file exists at src/main/resources/database.properties " +
                    "and the project is rebuilt (mvn clean compile)."
                );
            }

            props.load(is);
            is.close();

            // Load JDBC driver
            String driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            Class.forName(driver);

            // Read connection properties
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.username");
            PASSWORD = props.getProperty("db.password", "");

            // Diagnostic log (never print the actual password)
            System.out.println("[DBConnection] Driver  : " + driver);
            System.out.println("[DBConnection] URL     : " + URL);
            System.out.println("[DBConnection] User    : " + USER);
            System.out.println("[DBConnection] Password: " + (PASSWORD.isEmpty() ? "(empty)" : "(set)"));

        } catch (ClassNotFoundException e) {
            System.err.println("[DBConnection] JDBC driver class not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC driver not found. Check pom.xml for mysql-connector-j.", e);
        } catch (Exception e) {
            System.err.println("[DBConnection] Failed to load database configuration: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a new database connection.
     * Each call returns a fresh connection — caller is responsible for closing it.
     *
     * @return Connection object to the MySQL database
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        return conn;
    }

    /**
     * Test the database connection (useful for debugging).
     *
     * @return true if a connection can be established and is valid
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("[DBConnection] Connection test PASSED — connected to " + conn.getMetaData().getURL());
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("[DBConnection] Connection test FAILED: " + e.getMessage());
            return false;
        }
    }
}
