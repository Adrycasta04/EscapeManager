package it.unifi.escapemanager.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test strutturali (White-Box) per il calcolo del prezzo (Strategy Pattern).
 * Verifica ogni strategia di pricing concreta e il cambio di strategia a runtime.
 */
class PricingStrategyTest {

    @Test
    void testTariffaBaseCalcoloCorretto() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.setPricingStrategy(new TariffaBase());

        double risultato = stanza.calcolaPrezzo(4);

        assertEquals(80.0, risultato);
    }

    @Test
    void testTariffaWeekendMaggiorazione() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.setPricingStrategy(new TariffaWeekend());

        double risultato = stanza.calcolaPrezzo(4);

        // 20 * 4 * 1.20 = 96.0
        assertEquals(96.0, risultato, 0.01);
    }

    @Test
    void testCambioStrategiaARuntimeDaBaseAWeekend() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);

        // Strategia Base
        stanza.setPricingStrategy(new TariffaBase());
        assertEquals(40.0, stanza.calcolaPrezzo(2));

        // Cambio a runtime verso Weekend
        stanza.setPricingStrategy(new TariffaWeekend());
        assertEquals(48.0, stanza.calcolaPrezzo(2), 0.01);
    }

    @Test
    void testTariffaBaseConUnGiocatore() {
        PricingStrategy strategy = new TariffaBase();
        assertEquals(20.0, strategy.calcola(20.0, 1));
    }

    @Test
    void testTariffaWeekendConUnGiocatore() {
        PricingStrategy strategy = new TariffaWeekend();
        assertEquals(24.0, strategy.calcola(20.0, 1), 0.01);
    }
}
