package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.InCorsoState;
import it.unifi.escapemanager.domain.Stanza;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mock Test per GestioneStatoController.
 * Verifica le transizioni dello State Pattern in completo isolamento dal DB.
 */
@ExtendWith(MockitoExtension.class)
class GestioneStatoControllerMockTest {

    @Mock
    private StanzaDAO stanzaDAO;

    private GestioneStatoController controller;

    @BeforeEach
    void setUp() {
        controller = new GestioneStatoController(stanzaDAO);
    }

    @Test
    void testAvviaSessione_DaDisponibile_UC3_FlussoBase() {
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        when(stanzaDAO.findById("R01")).thenReturn(stanza);

        controller.eseguiTransizione("R01", "AVVIA");

        // Verifica che lo stato sia diventato IN_CORSO
        assertInstanceOf(InCorsoState.class, stanza.getStato());
        // Verifica che il DAO abbia persistito l'aggiornamento
        verify(stanzaDAO).update(stanza);
    }

    @Test
    void testTransizioneNonValida_TerminaSuDisponibile_UC3_Alt4a() {
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        when(stanzaDAO.findById("R01")).thenReturn(stanza);

        assertThrows(IllegalStateException.class,
                () -> controller.eseguiTransizione("R01", "TERMINA"));

        // Verifica che il DAO NON sia stato aggiornato
        verify(stanzaDAO, never()).update(any());
    }

    @Test
    void testStanzaNonTrovata() {
        when(stanzaDAO.findById("R99")).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> controller.eseguiTransizione("R99", "AVVIA"));
    }

    @Test
    void testComandoNonRiconosciuto() {
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        when(stanzaDAO.findById("R01")).thenReturn(stanza);

        assertThrows(IllegalArgumentException.class,
                () -> controller.eseguiTransizione("R01", "COMANDO_INVALIDO"));
    }
}
