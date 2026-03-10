package it.unifi.escapemanager.exceptions;

public class ValidationException extends EscapeManagerException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
