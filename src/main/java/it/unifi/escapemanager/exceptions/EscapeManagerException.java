package it.unifi.escapemanager.exceptions;

public class EscapeManagerException extends RuntimeException {
    public EscapeManagerException(String message) {
        super(message);
    }

    public EscapeManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
