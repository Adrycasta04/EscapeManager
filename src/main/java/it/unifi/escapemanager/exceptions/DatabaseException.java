package it.unifi.escapemanager.exceptions;

public class DatabaseException extends EscapeManagerException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
