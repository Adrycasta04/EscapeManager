package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.DAOFactory;
import it.unifi.escapemanager.dao.WaitingListDAO;
import it.unifi.escapemanager.exceptions.ValidationException;
import it.unifi.escapemanager.utils.DatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListaAttesaControllerTest {

    private ListaAttesaController controller;
    private WaitingListDAO waitingListDAO;

    @BeforeEach
    void setUp() {
        DatabaseHelper.resetTestDatabase();
        DAOFactory factory = DAOFactory.getDAOFactory();
        waitingListDAO = factory.getWaitingListDAO();
        controller = new ListaAttesaController(waitingListDAO);
    }

    @Test
    void testIscrizioneListaAttesa_OK() {
        controller.iscrivi("CLIENTE_01", "R01");
        assertTrue(waitingListDAO.exists("CLIENTE_01", "R01"));
    }

    @Test
    void testIscrizioneListaAttesa_Duplicato() {
        controller.iscrivi("CLIENTE_01", "R01");
        assertThrows(ValidationException.class, () -> controller.iscrivi("CLIENTE_01", "R01"));
    }
}
