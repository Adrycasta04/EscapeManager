package it.unifi.escapemanager.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
