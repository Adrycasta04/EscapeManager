package it.unifi.escapemanager.dao;

import it.unifi.escapemanager.domain.Stanza;
import java.util.List;

public interface StanzaDAO extends GenericDAO<Stanza, String> {
    // Metodo specifico: Trova tutte le stanze in una certa sede con un certo stato
    List<Stanza> findBySedeAndStato(String sedeId, String statoCorrente);
}