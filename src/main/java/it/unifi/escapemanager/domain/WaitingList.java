package it.unifi.escapemanager.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Subject del pattern Observer.
 * Gestisce la lista degli osservatori (clienti in attesa) e li notifica
 * quando uno slot viene liberato.
 */
public class WaitingList {

    private final List<WaitingListObserver> observers = new ArrayList<>();

    public void attach(WaitingListObserver o) {
        observers.add(o);
    }

    public void detach(WaitingListObserver o) {
        observers.remove(o);
    }

    public void notifyObservers(Stanza stanza, LocalDateTime slot) {
        for (WaitingListObserver o : observers) {
            o.update(stanza, slot);
        }
    }

    /**
     * Restituisce una vista non modificabile della lista degli observer (Copia Difensiva).
     * Questo impedisce al codice esterno di alterare la lista interna,
     * rispettando il principio di Information Hiding.
     */
    public List<WaitingListObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }

    /**
     * Restituisce il numero di observer attualmente registrati.
     */
    public int getObserverCount() {
        return observers.size();
    }
}
