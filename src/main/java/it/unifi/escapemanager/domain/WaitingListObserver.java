package it.unifi.escapemanager.domain;

import java.time.LocalDateTime;

public interface WaitingListObserver {
    void update(Stanza stanza, LocalDateTime slotLiberato);
}
