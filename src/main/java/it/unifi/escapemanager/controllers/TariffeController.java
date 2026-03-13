package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.Stanza;
import it.unifi.escapemanager.domain.TariffaBase;
import it.unifi.escapemanager.domain.TariffaWeekend;
import it.unifi.escapemanager.exceptions.ValidationException;

public class TariffeController {

    private final StanzaDAO stanzaDao;

    // Dependency Injection: DAO iniettato dal costruttore per facilitare il testing (Mockito)
    public TariffeController(StanzaDAO stanzaDao) {
        this.stanzaDao = stanzaDao;
    }

    public void cambiaTariffa(String stanzaId, String tipoTariffa) {
        Stanza stanza = stanzaDao.findById(stanzaId);
        if (stanza == null) {
            throw new IllegalArgumentException("Stanza non trovata.");
        }

        switch (tipoTariffa) {
            case "BASE":
                stanza.setPricingStrategy(new TariffaBase());
                break;
            case "WEEKEND":
                stanza.setPricingStrategy(new TariffaWeekend());
                break;
            default:
                throw new ValidationException("Tipo tariffa non valido");
        }

        stanzaDao.update(stanza);
    }
}
