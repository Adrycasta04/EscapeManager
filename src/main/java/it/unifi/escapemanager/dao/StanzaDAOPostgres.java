package it.unifi.escapemanager.dao;

import it.unifi.escapemanager.domain.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StanzaDAOPostgres implements StanzaDAO {

    private Connection getConnection() {
        return ConnectionManager.getInstance().getConnection();
    }

    @Override
    public void save(Stanza stanza) throws SQLException {
        String query = "INSERT INTO STANZA (id, sede_id, tema, capienza_max, prezzo_base, stato_corrente, pricing_strategy) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, stanza.getId());
            stmt.setString(2, stanza.getSedeId()); // Assumendo che tu aggiunga getSedeId() in Stanza
            stmt.setString(3, stanza.getTema());
            stmt.setInt(4, stanza.getCapienzaMax()); // Assumendo che tu aggiunga getCapienzaMax()
            stmt.setDouble(5, stanza.getPrezzoBase()); // Assumendo che tu aggiunga getPrezzoBase()
            stmt.setString(6, stanza.getStatoString());
            // Per ora salviamo una stringa fissa per la strategy, poi la miglioreremo
            stmt.setString(7, "BASE"); 
            stmt.executeUpdate();
        }
    }

    @Override
    public Stanza findById(String id) throws SQLException {
        String query = "SELECT * FROM STANZA WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStanza(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Stanza> findAll() throws SQLException {
        List<Stanza> stanze = new ArrayList<>();
        String query = "SELECT * FROM STANZA";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                stanze.add(mapResultSetToStanza(rs));
            }
        }
        return stanze;
    }

    @Override
    public void update(Stanza stanza) throws SQLException {
        String query = "UPDATE STANZA SET tema = ?, capienza_max = ?, prezzo_base = ?, stato_corrente = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, stanza.getTema());
            stmt.setInt(2, stanza.getCapienzaMax());
            stmt.setDouble(3, stanza.getPrezzoBase());
            stmt.setString(4, stanza.getStatoString());
            stmt.setString(5, stanza.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String query = "DELETE FROM STANZA WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Stanza> findBySedeAndStato(String sedeId, String statoCorrente) throws SQLException {
        List<Stanza> stanze = new ArrayList<>();
        String query = "SELECT * FROM STANZA WHERE sede_id = ? AND stato_corrente = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, sedeId);
            stmt.setString(2, statoCorrente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stanze.add(mapResultSetToStanza(rs));
                }
            }
        }
        return stanze;
    }

    // Metodo Helper per tradurre una riga SQL in un Oggetto Java
    private Stanza mapResultSetToStanza(ResultSet rs) throws SQLException {
        Stanza stanza = new Stanza(
            rs.getString("id"),
            rs.getString("sede_id"),
            rs.getString("tema"),
            rs.getInt("capienza_max"),
            rs.getDouble("prezzo_base")
        );
        
        // Ricostruzione dello State Pattern partendo dal Database! (Factory Method informale)
        String statoDb = rs.getString("stato_corrente");
        if ("IN_CORSO".equals(statoDb)) {
            stanza.setStato(new InCorsoState()); // Decommenta quando crei la classe
        } else if ("IN_MANUTENZIONE".equals(statoDb)) {
            stanza.setStato(new InManutenzioneState()); // Decommenta quando crei la classe
        } else if ("IN_PULIZIA".equals(statoDb)) {
            stanza.setStato(new InPuliziaState()); // Decommenta quando crei la classe
        } else {
            stanza.setStato(new DisponibileState());
        }

        return stanza;
    }
}