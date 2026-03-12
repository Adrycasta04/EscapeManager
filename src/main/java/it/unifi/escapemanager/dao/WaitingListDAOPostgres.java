package it.unifi.escapemanager.dao;

import it.unifi.escapemanager.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class WaitingListDAOPostgres implements WaitingListDAO {

    private Connection getConnection() {
        return ConnectionManager.getInstance().getConnection();
    }

    @Override
    public void add(String clienteId, String stanzaId, LocalDateTime dataRichiesta) {
        String query = "INSERT INTO LISTA_ATTESA (cliente_id, stanza_id, data_richiesta) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, clienteId);
            stmt.setString(2, stanzaId);
            stmt.setTimestamp(3, Timestamp.valueOf(dataRichiesta));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Errore di accesso ai dati: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(String clienteId, String stanzaId) {
        String query = "SELECT COUNT(*) FROM LISTA_ATTESA WHERE cliente_id = ? AND stanza_id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, clienteId);
            stmt.setString(2, stanzaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore di accesso ai dati: " + e.getMessage(), e);
        }
        return false;
    }
}
