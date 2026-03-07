package it.unifi.escapemanager.exceptions;

/**
 * Eccezione lanciata quando si tenta di prenotare uno slot orario già occupato.
 * Corrisponde al Flusso Alternativo 3a dell'UC2 (Race Condition).
 */
public class SlotNonDisponibileException extends Exception {
    public SlotNonDisponibileException(String message) {
        super(message);
    }
}
