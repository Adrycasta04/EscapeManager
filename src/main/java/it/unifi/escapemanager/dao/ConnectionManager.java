package it.unifi.escapemanager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    // Istanza statica unica (Singleton)
    private static ConnectionManager instance;
    private Connection connection;

    // Ho inserito qui le mie credenziali di Postgres
    private static final String URL = "jdbc:postgresql://localhost:5432/escapemanager";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Cirodino1!";

    // Costruttore privato! Nessuno può fare 'new ConnectionManager()' da fuori
    private ConnectionManager() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Errore di connessione al DB: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Metodo per ottenere l'istanza
    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    // Metodo per ottenere la connessione JDBC
    public Connection getConnection() {
        try {
            // Se la connessione è caduta, la riapriamo
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore nel ripristino della connessione", e);
        }
        return connection;
    }
}