package com.abonnement.databaseSetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSetup {

    private static final String URL = "jdbc:postgresql://localhost:5432/abonnement";
    private static final String USER = "postgres";
    private static final String PASSWORD = "ME551234";
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new SQLException("PostgreSQL driver not found", e);
        }
    }
}
