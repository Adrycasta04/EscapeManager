package it.unifi.escapemanager.dao;

import it.unifi.escapemanager.domain.Prenotazione;
import it.unifi.escapemanager.exceptions.DatabaseException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAOPostgres implements PrenotazioneDAO {

    private Connection getConnection() {
        return ConnectionManager.getInstance().getConnection();
    }

    @Override
    public void save(Prenotazione p) {
        String query = "INSERT INTO PRENOTAZIONE (id, cliente_id, stanza_id, data_ora, numero_giocatori, prezzo_totale, stato_partita) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, p.getId());
            stmt.setString(2, p.getClienteId());
            stmt.setString(3, p.getStanzaId());
            stmt.setTimestamp(4, Timestamp.valueOf(p.getDataOra()));
            stmt.setInt(5, p.getNumeroGiocatori());
            stmt.setDouble(6, p.getPrezzoTotale());
            stmt.setString(7, p.getStatoPartita());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Errore di accesso ai dati: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isSlotOccupato(String stanzaId, LocalDateTime dataOra) {
        String query = "SELECT COUNT(*) FROM PRENOTAZIONE WHERE stanza_id = ? AND data_ora = ? AND stato_partita != 'ANNULLATA'";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, stanzaId);
            stmt.setTimestamp(2, Timestamp.valueOf(dataOra));
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

    @Override
    public Prenotazione findById(String id) {
        String query = "SELECT * FROM PRENOTAZIONE WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPrenotazione(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore di accesso ai dati: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Prenotazione> findByStanzaId(String stanzaId) {
        List<Prenotazione> list = new ArrayList<>();
        String query = "SELECT * FROM PRENOTAZIONE WHERE stanza_id = ? ORDER BY data_ora ASC";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, stanzaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPrenotazione(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore di accesso ai dati: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Prenotazione> findAll() {
        List<Prenotazione> list = new ArrayList<>();
        String query = "SELECT * FROM PRENOTAZIONE";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(mapResultSetToPrenotazione(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore di accesso ai dati: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(Prenotazione p) {
        String query = "UPDATE PRENOTAZIONE SET stato_partita = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, p.getStatoPartita());
            stmt.setString(2, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Errore di accesso ai dati: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String query = "DELETE FROM PRENOTAZIONE WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Errore di accesso ai dati: " + e.getMessage(), e);
        }
    }

    // Helper per mappare la riga SQL nell'oggetto Java
    private Prenotazione mapResultSetToPrenotazione(ResultSet rs) throws SQLException {
        return new Prenotazione(
            rs.getString("id"),
            rs.getString("cliente_id"),
            rs.getString("stanza_id"),
            rs.getTimestamp("data_ora").toLocalDateTime(), // Conversione SQL -> Java
            rs.getInt("numero_giocatori"),
            rs.getDouble("prezzo_totale"),
            rs.getString("stato_partita")
        );
    }
}