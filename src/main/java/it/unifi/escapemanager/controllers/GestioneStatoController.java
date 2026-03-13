package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.Stanza;

public class GestioneStatoController {

    private final StanzaDAO stanzaDao;

    // Dependency Injection: DAO iniettato dal costruttore per facilitare il testing (Mockito)
    public GestioneStatoController(StanzaDAO stanzaDao) {
        this.stanzaDao = stanzaDao;
    }

    public void eseguiTransizione(String stanzaId, String comando) {
        Stanza stanza = stanzaDao.findById(stanzaId);
        if (stanza == null) {
            throw new IllegalArgumentException("Stanza non trovata.");
        }

        switch (comando) {
            case "AVVIA":
                stanza.iniziaSessione();
                break;
            case "TERMINA":
                stanza.terminaSessione();
                break;
            case "MANUTENZIONE":
                stanza.impostaManutenzione();
                break;
            case "PULISCI":
                stanza.impostaDisponibile();
                break;
            default:
                throw new IllegalArgumentException("Comando non riconosciuto: " + comando);
        }

        stanzaDao.update(stanza);
    }
}
