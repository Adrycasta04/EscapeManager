package it.unifi.escapemanager.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test strutturali (White-Box) per il Builder Pattern della Prenotazione.
 * Verifica la creazione fluente e la corretta assegnazione dei campi.
 */
class PrenotazioneBuilderTest {

    @Test
    void testBuilderCreaPrenotazioneCompleta() {
        LocalDateTime dataOra = LocalDateTime.of(2026, 10, 31, 21, 0);

        Prenotazione p = new Prenotazione.Builder()
                .id("P001")
                .clienteId("C01")
                .stanzaId("R01")
                .dataOra(dataOra)
                .numeroGiocatori(4)
                .prezzoTotale(80.0)
                .statoPartita(StatoPartita.CONFERMATA)
                .build();

        assertEquals("P001", p.getId());
        assertEquals("C01", p.getClienteId());
        assertEquals("R01", p.getStanzaId());
        assertEquals(dataOra, p.getDataOra());
        assertEquals(4, p.getNumeroGiocatori());
        assertEquals(80.0, p.getPrezzoTotale());
        assertEquals(StatoPartita.CONFERMATA, p.getStatoPartita());
    }

    @Test
    void testBuilderConfrontoConCostruttoreDiretto() {
        LocalDateTime dataOra = LocalDateTime.of(2026, 10, 31, 21, 0);

        Prenotazione daDiretto = new Prenotazione("P001", "C01", "R01", dataOra, 4, 80.0, StatoPartita.CONFERMATA);
        Prenotazione daBuilder = new Prenotazione.Builder()
                .id("P001")
                .clienteId("C01")
                .stanzaId("R01")
                .dataOra(dataOra)
                .numeroGiocatori(4)
                .prezzoTotale(80.0)
                .statoPartita(StatoPartita.CONFERMATA)
                .build();

        assertEquals(daDiretto.getId(), daBuilder.getId());
        assertEquals(daDiretto.getClienteId(), daBuilder.getClienteId());
        assertEquals(daDiretto.getPrezzoTotale(), daBuilder.getPrezzoTotale());
    }
}
