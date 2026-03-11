package it.unifi.escapemanager.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StanzaTest {

    @Test
    void testTransizioneCorretta() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);

        stanza.iniziaSessione();

        assertInstanceOf(InCorsoState.class, stanza.getStato());
    }

    @Test
    void testEccezioneStatoNonValido() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.iniziaSessione();

        assertThrows(IllegalStateException.class, () -> stanza.iniziaSessione());
    }
}
