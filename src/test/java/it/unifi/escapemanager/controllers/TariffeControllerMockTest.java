package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.Stanza;
import it.unifi.escapemanager.domain.TariffaBase;
import it.unifi.escapemanager.domain.TariffaWeekend;
import it.unifi.escapemanager.exceptions.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mock Test per TariffeController.
 * Verifica la logica di configurazione delle tariffe (UC4) in isolamento dal DB.
 */
@ExtendWith(MockitoExtension.class)
class TariffeControllerMockTest {

    @Mock
    private StanzaDAO stanzaDAO;

    private TariffeController controller;

    @BeforeEach
    void setUp() {
        controller = new TariffeController(stanzaDAO);
    }

    @Test
    void testCambioTariffaWeekend_UC4_FlussoBase() {
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        when(stanzaDAO.findById("R01")).thenReturn(stanza);

        controller.cambiaTariffa("R01", "WEEKEND");

        assertInstanceOf(TariffaWeekend.class, stanza.getPricingStrategy());
        verify(stanzaDAO).update(stanza);
    }

    @Test
    void testCambioTariffaBase_UC4_FlussoBase() {
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        stanza.setPricingStrategy(new TariffaWeekend()); // Parto da Weekend
        when(stanzaDAO.findById("R01")).thenReturn(stanza);

        controller.cambiaTariffa("R01", "BASE");

        assertInstanceOf(TariffaBase.class, stanza.getPricingStrategy());
        verify(stanzaDAO).update(stanza);
    }

    @Test
    void testTipoTariffaNonValido() {
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        when(stanzaDAO.findById("R01")).thenReturn(stanza);

        assertThrows(ValidationException.class,
                () -> controller.cambiaTariffa("R01", "NOTTURNA"));
    }

    @Test
    void testStanzaNonTrovata() {
        when(stanzaDAO.findById("R99")).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> controller.cambiaTariffa("R99", "BASE"));
    }
}
