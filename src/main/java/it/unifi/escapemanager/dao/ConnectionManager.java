package it.unifi.escapemanager.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    // Istanza statica unica (Singleton)
    private static ConnectionManager instance;
    private Connection connection;

    private static final String PROP_FILE = "db.properties";
    private static final String ENV_URL = "EM_DB_URL";
    private static final String ENV_USER = "EM_DB_USER";
    private static final String ENV_PASSWORD = "EM_DB_PASSWORD";

    private final String url;
    private final String user;
    private final String password;

    // Costruttore privato! Nessuno puo fare 'new ConnectionManager()' da fuori
    private ConnectionManager() {
        Properties props = loadProperties();
        this.url = firstNonBlank(System.getenv(ENV_URL),
                System.getProperty("db.url"),
                props.getProperty("db.url"),
                "jdbc:postgresql://localhost:5432/escapemanager");
        this.user = firstNonBlank(System.getenv(ENV_USER),
                System.getProperty("db.user"),
                props.getProperty("db.user"),
                "postgres");
        this.password = firstNonBlank(System.getenv(ENV_PASSWORD),
                System.getProperty("db.password"),
                props.getProperty("db.password"),
                "");
        try {
            this.connection = DriverManager.getConnection(url, user, password);
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
            // Se la connessione e caduta, la riapriamo
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore nel ripristino della connessione", e);
        }
        return connection;
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = ConnectionManager.class.getClassLoader().getResourceAsStream(PROP_FILE)) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            System.err.println("Impossibile leggere " + PROP_FILE + ": " + e.getMessage());
        }
        return props;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }
}