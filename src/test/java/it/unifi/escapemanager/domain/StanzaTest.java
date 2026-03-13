package it.unifi.escapemanager.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test strutturali (White-Box) per le transizioni di stato della Stanza (State Pattern).
 * Verifica tutte le transizioni valide e le eccezioni per transizioni non consentite.
 */
class StanzaTest {

    // --- Transizioni valide ---

    @Test
    void testTransizioneCorretta() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.iniziaSessione();
        assertInstanceOf(InCorsoState.class, stanza.getStato());
    }

    @Test
    void testTransizioneInCorso_TerminaSessione_VaInPulizia() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.iniziaSessione();
        stanza.terminaSessione();
        assertInstanceOf(InPuliziaState.class, stanza.getStato());
        assertEquals("IN_PULIZIA", stanza.getStatoString());
    }

    @Test
    void testTransizioneInPulizia_ImpostaDisponibile() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.iniziaSessione();
        stanza.terminaSessione();
        stanza.impostaDisponibile();
        assertInstanceOf(DisponibileState.class, stanza.getStato());
        assertEquals("DISPONIBILE", stanza.getStatoString());
    }

    @Test
    void testTransizioneDisponibile_ImpostaManutenzione() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.impostaManutenzione();
        assertInstanceOf(InManutenzioneState.class, stanza.getStato());
        assertEquals("IN_MANUTENZIONE", stanza.getStatoString());
    }

    @Test
    void testTransizioneManutenzione_ImpostaDisponibile() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.impostaManutenzione();
        stanza.impostaDisponibile();
        assertInstanceOf(DisponibileState.class, stanza.getStato());
    }

    @Test
    void testCicloCompletoStati() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        assertEquals("DISPONIBILE", stanza.getStatoString());

        stanza.iniziaSessione();
        assertEquals("IN_CORSO", stanza.getStatoString());

        stanza.terminaSessione();
        assertEquals("IN_PULIZIA", stanza.getStatoString());

        stanza.impostaDisponibile();
        assertEquals("DISPONIBILE", stanza.getStatoString());
    }

    // --- Transizioni non valide (eccezioni) ---

    @Test
    void testEccezioneStatoNonValido() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.iniziaSessione();
        assertThrows(IllegalStateException.class, () -> stanza.iniziaSessione());
    }

    @Test
    void testEccezione_TerminaSessione_SuDisponibile() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        assertThrows(IllegalStateException.class, () -> stanza.terminaSessione());
    }

    @Test
    void testEccezione_Prenota_SuInManutenzione() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.impostaManutenzione();
        assertThrows(IllegalStateException.class, () -> stanza.prenota());
    }

    @Test
    void testEccezione_SetDisponibile_SuDisponibile() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        assertThrows(IllegalStateException.class, () -> stanza.impostaDisponibile());
    }

    // --- Stato iniziale ---

    @Test
    void testStatoInizialeDisponibile() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        assertInstanceOf(DisponibileState.class, stanza.getStato());
        assertEquals("DISPONIBILE", stanza.getStatoString());
    }
}
