package it.unifi.escapemanager.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingStrategyTest {

    @Test
    void testTariffaBaseCalcoloCorretto() {
        Stanza stanza = new Stanza("T01", "FI01", "Test Room", 6, 20.0);
        stanza.setPricingStrategy(new TariffaBase());

        double risultato = stanza.calcolaPrezzo(4);

        assertEquals(80.0, risultato);
    }
}
