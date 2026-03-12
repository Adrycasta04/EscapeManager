package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.WaitingListDAO;
import it.unifi.escapemanager.exceptions.ValidationException;
import java.time.LocalDateTime;

public class ListaAttesaController {

    private final WaitingListDAO waitingListDAO;

    public ListaAttesaController(WaitingListDAO waitingListDAO) {
        this.waitingListDAO = waitingListDAO;
    }

    public void iscrivi(String clienteId, String stanzaId) {
        if (waitingListDAO.exists(clienteId, stanzaId)) {
            throw new ValidationException("Cliente gia' iscritto alla lista d'attesa per questa stanza.");
        }
        waitingListDAO.add(clienteId, stanzaId, LocalDateTime.now());
    }
}
