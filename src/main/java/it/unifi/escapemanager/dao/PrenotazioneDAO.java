package it.unifi.escapemanager.dao;

import it.unifi.escapemanager.domain.Prenotazione;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface PrenotazioneDAO extends GenericDAO<Prenotazione, String> {
    
    // Metodo cruciale per la logica di business: verifica le collisioni di orario
    boolean isSlotOccupato(String stanzaId, LocalDateTime dataOra) throws SQLException;
    
    // Metodo per trovare tutte le prenotazioni di una specifica stanza (utile per il Game Master)
    List<Prenotazione> findByStanzaId(String stanzaId) throws SQLException;
}