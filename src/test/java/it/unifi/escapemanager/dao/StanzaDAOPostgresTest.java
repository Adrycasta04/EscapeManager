package it.unifi.escapemanager.dao;

import it.unifi.escapemanager.domain.Stanza;
import it.unifi.escapemanager.utils.DatabaseHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StanzaDAOPostgresTest {

    private StanzaDAO stanzaDao;

    @BeforeEach
    void setUp() {
        DatabaseHelper.resetTestDatabase();
        stanzaDao = DAOFactory.getDAOFactory().getStanzaDAO();
    }

    @Test
    void testSaveAndFindById() {
        // b) Recupera la stanza di test inserita dal DatabaseHelper (id: "R01")
        Stanza stanza = stanzaDao.findById("R01");
        assertNotNull(stanza);
        assertEquals("Horror Asylum", stanza.getTema());
        assertEquals(20.0, stanza.getPrezzoBase());

        // c) Modifica il prezzo della stanza a 25.0 e aggiorna
        Stanza stanzaAggiornata = new Stanza("R01", stanza.getSedeId(), stanza.getTema(),
                stanza.getCapienzaMax(), 25.0);
        stanzaDao.update(stanzaAggiornata);

        // d) Verifica che il prezzo aggiornato sia 25.0 nel database
        Stanza stanzaRicaricata = stanzaDao.findById("R01");
        assertEquals(25.0, stanzaRicaricata.getPrezzoBase());
    }
}
