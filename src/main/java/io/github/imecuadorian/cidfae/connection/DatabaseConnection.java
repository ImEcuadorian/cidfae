package io.github.imecuadorian.cidfae.connection;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.*;

public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static final Dotenv DOTENV = Dotenv.load();
    private static final String URL = DOTENV.get("DB_URL");
    private static final String USER = DOTENV.get("DB_USER");
    private static final String PASSWORD = DOTENV.get("DB_PASSWORD");

    private static Connection connection;

    private DatabaseConnection() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                LOGGER.info("✅ Database connection established successfully.");
            } catch (ClassNotFoundException e) {
                LOGGER.severe("❌ MySQL JDBC Driver not found.");
                LOGGER.severe(e.getMessage());
            } catch (SQLException e) {
                LOGGER.severe("❌ Database connection failed.");
                LOGGER.severe(e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.info("✅ Database connection closed successfully.");
            }
        } catch (SQLException e) {
            LOGGER.severe("❌ Error closing database connection.");
            LOGGER.severe(e.getMessage());
        } finally {
            connection = null;
        }
    }
}
