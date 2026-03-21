package it.unifi.escapemanager.dao;

import it.unifi.escapemanager.domain.Prenotazione;
import it.unifi.escapemanager.domain.StatoPartita;
import it.unifi.escapemanager.utils.DatabaseHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PrenotazioneDAOPostgresTest {

    private PrenotazioneDAO prenotazioneDao;

    @BeforeEach
    void setUp() {
        DatabaseHelper.resetTestDatabase();
        prenotazioneDao = DAOFactory.getDAOFactory().getPrenotazioneDAO();
    }

    @Test
    void testIsSlotOccupato() {
        // R01 e C01 sono gia presenti grazie al resetTestDatabase()
        LocalDateTime dataOra = LocalDateTime.of(2026, 10, 31, 21, 0);

        // All'inizio lo slot non e occupato
        assertFalse(prenotazioneDao.isSlotOccupato("R01", dataOra));

        // Inseriamo una prenotazione CONFERMATA per quello slot
        Prenotazione p = new Prenotazione.Builder()
                .id("P99")
                .clienteId("CLIENTE_01")
                .stanzaId("R01")
                .dataOra(dataOra)
                .numeroGiocatori(4)
                .prezzoTotale(80.0)
                .statoPartita(StatoPartita.CONFERMATA)
                .build();
        
        prenotazioneDao.save(p);

        // Ora lo slot deve risultare occupato
        assertTrue(prenotazioneDao.isSlotOccupato("R01", dataOra));

        // Se la prenotazione viene ANNULLATA, lo slot torna libero
        p.setStatoPartita(StatoPartita.ANNULLATA);
        prenotazioneDao.update(p);
        
        // Verifica che lo slot sia di nuovo disponibile (esclude le ANNULLATA)
        assertFalse(prenotazioneDao.isSlotOccupato("R01", dataOra));
    }
}
