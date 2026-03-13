package it.unifi.escapemanager.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test strutturali (White-Box) per il meccanismo Observer della Lista d'Attesa.
 * Verifica attach, detach, e notifica degli observer.
 */
class WaitingListObserverTest {

    @Test
    void testAttachENotifica() {
        WaitingList waitingList = new WaitingList();
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        Cliente cliente = new Cliente("C01", "c@test.it", "hash", "Mario", "333");

        waitingList.attach(cliente);

        LocalDateTime slot = LocalDateTime.of(2026, 10, 31, 21, 0);
        waitingList.notifyObservers(stanza, slot);

        assertNotNull(cliente.getUltimaNotifica());
        assertTrue(cliente.getUltimaNotifica().contains("Horror"));
    }

    @Test
    void testDetachNonRiceveNotifica() {
        WaitingList waitingList = new WaitingList();
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        Cliente cliente = new Cliente("C01", "c@test.it", "hash", "Mario", "333");

        waitingList.attach(cliente);
        waitingList.detach(cliente);

        waitingList.notifyObservers(stanza, LocalDateTime.now());

        assertNull(cliente.getUltimaNotifica());
    }

    @Test
    void testNotificaMultipleObservers() {
        WaitingList waitingList = new WaitingList();
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);
        Cliente c1 = new Cliente("C01", "c1@test.it", "hash", "Mario", "333");
        Cliente c2 = new Cliente("C02", "c2@test.it", "hash", "Luigi", "444");

        waitingList.attach(c1);
        waitingList.attach(c2);

        waitingList.notifyObservers(stanza, LocalDateTime.now());

        assertNotNull(c1.getUltimaNotifica());
        assertNotNull(c2.getUltimaNotifica());
    }

    @Test
    void testNotificaSenzaObservers() {
        WaitingList waitingList = new WaitingList();
        Stanza stanza = new Stanza("R01", "FI01", "Horror", 6, 20.0);

        // Non deve lanciare eccezioni
        assertDoesNotThrow(() -> waitingList.notifyObservers(stanza, LocalDateTime.now()));
    }
}
