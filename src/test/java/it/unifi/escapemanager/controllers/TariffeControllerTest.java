package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.DAOFactory;
import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.Stanza;
import it.unifi.escapemanager.domain.TariffaWeekend;
import it.unifi.escapemanager.utils.DatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TariffeControllerTest {

    private TariffeController controller;
    private StanzaDAO stanzaDao;

    @BeforeEach
    void setUp() {
        DatabaseHelper.resetTestDatabase();
        controller = new TariffeController();
        stanzaDao = DAOFactory.getDAOFactory().getStanzaDAO();
    }

    @Test
    void testCambioTariffaWeekend_UC4() {
        controller.cambiaTariffa("R01", "WEEKEND");
        Stanza stanza = stanzaDao.findById("R01");
        assertTrue(stanza.getPricingStrategy() instanceof TariffaWeekend);
    }
}