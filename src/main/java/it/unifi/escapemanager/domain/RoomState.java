package it.unifi.escapemanager.domain;

public interface RoomState {
    void prenota(Stanza stanza);
    void iniziaSessione(Stanza stanza);
    void terminaSessione(Stanza stanza);
    void setManutenzione(Stanza stanza);
    String getStatusString(); // Utile per salvare lo stato sul Database
}