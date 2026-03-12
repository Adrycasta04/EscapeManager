package it.unifi.escapemanager.dao;

import java.time.LocalDateTime;

public interface WaitingListDAO {
    void add(String clienteId, String stanzaId, LocalDateTime dataRichiesta);
    boolean exists(String clienteId, String stanzaId);
}
