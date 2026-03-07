package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.PrenotazioneDAOPostgres;
import it.unifi.escapemanager.dao.StanzaDAOPostgres;
import it.unifi.escapemanager.domain.Prenotazione;
import it.unifi.escapemanager.exceptions.SlotNonDisponibileException;
import it.unifi.escapemanager.utils.DatabaseHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrenotazioneControllerTest {

    private PrenotazioneController controller;
    private PrenotazioneDAOPostgres prenotazioneDAO;

    @BeforeEach
    void setUp() {
        DatabaseHelper.resetTestDatabase();
        prenotazioneDAO = new PrenotazioneDAOPostgres();
        StanzaDAOPostgres stanzaDAO = new StanzaDAOPostgres();
        controller = new PrenotazioneController(prenotazioneDAO, stanzaDAO);
    }

    @Test
    void testPrenotazioneNegataPerSlotOccupato_UC1_Alt3a() throws SQLException {
        String stanzaId = "R01";
        LocalDateTime slotRichiesto = LocalDateTime.of(2026, 10, 31, 21, 0);

        // Inserisci manualmente una prenotazione per occupare lo slot
        Prenotazione esistente = new Prenotazione(
            UUID.randomUUID().toString(),
            "CLIENTE_01",
            stanzaId,
            slotRichiesto,
            3,
            60.0,
            "CONFERMATA"
        );
        prenotazioneDAO.save(esistente);

        // La seconda prenotazione sullo stesso slot deve fallire
        SlotNonDisponibileException ex = assertThrows(
            SlotNonDisponibileException.class,
            () -> controller.effettuaPrenotazione("CLIENTE_02", stanzaId, slotRichiesto, 4)
        );

        assertEquals("Lo slot orario selezionato non e' piu' disponibile.", ex.getMessage());
    }
}
