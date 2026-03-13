package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.PrenotazioneDAO;
import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.Prenotazione;
import it.unifi.escapemanager.domain.Stanza;
import it.unifi.escapemanager.exceptions.SlotNonDisponibileException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mock Test per PrenotazioneController.
 * Usa Mockito per isolare il Controller dalle dipendenze DB (DAO),
 * testando solo la logica di business in completo isolamento.
 */
@ExtendWith(MockitoExtension.class)
class PrenotazioneControllerMockTest {

    @Mock
    private PrenotazioneDAO prenotazioneDAO;

    @Mock
    private StanzaDAO stanzaDAO;

    private PrenotazioneController controller;

    @BeforeEach
    void setUp() {
        controller = new PrenotazioneController(prenotazioneDAO, stanzaDAO);
    }

    @Test
    void testPrenotazioneRiuscita_FlussoBase_UC1() {
        // Arrange: la stanza esiste ed è disponibile
        Stanza stanza = new Stanza("R01", "FI01", "Horror Asylum", 6, 20.0);
        when(stanzaDAO.findById("R01")).thenReturn(stanza);
        when(prenotazioneDAO.isSlotOccupato(eq("R01"), any(LocalDateTime.class))).thenReturn(false);

        LocalDateTime slot = LocalDateTime.of(2026, 10, 31, 21, 0);

        // Act
        Prenotazione result = controller.effettuaPrenotazione("C01", "R01", slot, 4);

        // Assert
        assertNotNull(result);
        assertEquals("C01", result.getClienteId());
        assertEquals("R01", result.getStanzaId());
        assertEquals(80.0, result.getPrezzoTotale()); // 20 * 4 = 80 con TariffaBase (default)
        assertEquals("CONFERMATA", result.getStatoPartita());

        // Verify: il DAO di salvataggio è stato invocato esattamente 1 volta
        verify(prenotazioneDAO, times(1)).save(any(Prenotazione.class));
    }

    @Test
    void testPrenotazioneNegata_SlotOccupato_UC1_Alt3a() {
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        when(stanzaDAO.findById("R01")).thenReturn(stanza);
        when(prenotazioneDAO.isSlotOccupato(eq("R01"), any())).thenReturn(true);

        assertThrows(SlotNonDisponibileException.class,
                () -> controller.effettuaPrenotazione("C01", "R01", LocalDateTime.now(), 4));

        // Verify: il DAO di salvataggio NON è stato invocato
        verify(prenotazioneDAO, never()).save(any());
    }

    @Test
    void testPrenotazioneNegata_StanzaInesistente() {
        when(stanzaDAO.findById("R99")).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> controller.effettuaPrenotazione("C01", "R99", LocalDateTime.now(), 4));
    }

    @Test
    void testPrenotazioneNegata_CapienzaSuperata_UC1_Alt4a() {
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        when(stanzaDAO.findById("R01")).thenReturn(stanza);

        // 10 giocatori > capienza max 6
        assertThrows(IllegalArgumentException.class,
                () -> controller.effettuaPrenotazione("C01", "R01", LocalDateTime.now(), 10));
    }

    @Test
    void testPrenotazioneNegata_NumeroGiocatoriZero() {
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        when(stanzaDAO.findById("R01")).thenReturn(stanza);

        assertThrows(IllegalArgumentException.class,
                () -> controller.effettuaPrenotazione("C01", "R01", LocalDateTime.now(), 0));
    }
}
