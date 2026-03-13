package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.PrenotazioneDAO;
import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.Prenotazione;
import it.unifi.escapemanager.domain.Stanza;
import it.unifi.escapemanager.domain.StatoPartita;
import it.unifi.escapemanager.exceptions.SlotNonDisponibileException;

import java.time.LocalDateTime;
import java.util.UUID;

public class PrenotazioneController {
    
    private final PrenotazioneDAO prenotazioneDAO;
    private final StanzaDAO stanzaDAO;

    // Dependency Injection: Passiamo i DAO dal costruttore per facilitare i Test (Mocking)
    public PrenotazioneController(PrenotazioneDAO prenotazioneDAO, StanzaDAO stanzaDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.stanzaDAO = stanzaDAO;
    }

    /**
     * Implementa il Caso d'Uso: UC1 - Prenotazione Stanza
     */
    public Prenotazione effettuaPrenotazione(String clienteId, String stanzaId, LocalDateTime dataOra, int numeroGiocatori) {
        
        // 1. Recupero dei dati (DAO Pattern)
        Stanza stanza = stanzaDAO.findById(stanzaId);
        if (stanza == null) {
            throw new IllegalArgumentException("Errore: Stanza non trovata nel catalogo.");
        }

        // 2. Validazione di Business (Capienza) - UC1 Flusso Alternativo 4a
        if (numeroGiocatori <= 0 || numeroGiocatori > stanza.getCapienzaMax()) {
            throw new IllegalArgumentException("Numero giocatori non valido. La stanza " 
                + stanza.getTema() + " ospita massimo " + stanza.getCapienzaMax() + " persone.");
        }

        // ---> IL CONTROLLO MAGICO DELLO STATE PATTERN <---
        // Se la stanza è in manutenzione o in corso, questo metodo lancerà in automatico 
        // una IllegalStateException! Nessun "if" necessario.
        stanza.prenota();

        // 3. Controllo della Race Condition sul Database - UC1 Flusso Alternativo 3a
        if (prenotazioneDAO.isSlotOccupato(stanzaId, dataOra)) {
            // Lanciamo l'eccezione custom creata apposta per questo dominio!
            throw new SlotNonDisponibileException("Lo slot orario selezionato non e' piu' disponibile.");
        }

        // 4. Calcolo del prezzo (La "Magia" dello Strategy Pattern)
        // Il controller non sa *come* si calcola il prezzo, delega tutto alla Stanza
        // che a sua volta usa la sua PricingStrategy corrente.
        double prezzoTotale = stanza.calcolaPrezzo(numeroGiocatori);

        // 5. Creazione della nuova entità (UUID per avere un ID univoco e sicuro)
        String nuovaPrenotazioneId = UUID.randomUUID().toString();
        Prenotazione nuovaPrenotazione = new Prenotazione(
            nuovaPrenotazioneId,
            clienteId,
            stanzaId,
            dataOra,
            numeroGiocatori,
            prezzoTotale,
            StatoPartita.CONFERMATA
        );

        // 6. Salvataggio definitivo (Persistenza)
        prenotazioneDAO.save(nuovaPrenotazione);

        return nuovaPrenotazione;
    }
}