package it.unifi.escapemanager.utils;

import it.unifi.escapemanager.dao.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    public static void resetTestDatabase() {
        try {
            Connection conn = ConnectionManager.getInstance().getConnection();
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE LISTA_ATTESA, PRENOTAZIONE, UTENTE, STANZA, SEDE CASCADE;");
                stmt.execute("INSERT INTO SEDE (id, nome, citta) VALUES ('FI01', 'Sede Firenze', 'Firenze');");
                stmt.execute("INSERT INTO STANZA (id, sede_id, tema, capienza_max, prezzo_base, stato_corrente, pricing_strategy) VALUES ('R01', 'FI01', 'Horror Asylum', 6, 20.0, 'DISPONIBILE', 'BASE');");
                stmt.execute("INSERT INTO UTENTE (id, email, password_hash, ruolo, nome, telefono) VALUES ('CLIENTE_01', 'cliente1@test.it', 'hash123', 'CLIENTE', 'Mario Rossi', '3331234567');");
                stmt.execute("INSERT INTO UTENTE (id, email, password_hash, ruolo, nome, telefono) VALUES ('CLIENTE_02', 'cliente2@test.it', 'hash456', 'CLIENTE', 'Luigi Verdi', '3339876543');");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
