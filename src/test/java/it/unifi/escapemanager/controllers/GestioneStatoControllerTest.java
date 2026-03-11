package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.DAOFactory;
import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.Stanza;
import it.unifi.escapemanager.utils.DatabaseHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GestioneStatoControllerTest {

    private GestioneStatoController controller;
    private StanzaDAO stanzaDao;

    @BeforeEach
    void setUp() {
        DatabaseHelper.resetTestDatabase();
        controller = new GestioneStatoController();
        stanzaDao = DAOFactory.getDAOFactory().getStanzaDAO();
    }

    @Test
    void testEseguiTransizioneAvvia() {
        controller.eseguiTransizione("R01", "AVVIA");

        Stanza stanza = stanzaDao.findById("R01");
        assertEquals("IN_CORSO", stanza.getStatoString());
    }
}
