package it.unifi.escapemanager.cli;

import it.unifi.escapemanager.dao.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe di utilità che popola il database con dati dimostrativi realistici
 * se le tabelle risultano vuote. Viene invocata all'avvio della CLI per
 * garantire un'esperienza utente significativa durante la demo del sistema.
 */
public class DemoDataSeeder {

    /**
     * Verifica se il database contiene almeno una stanza.
     * Se non ne trova, inserisce un set completo di dati demo.
     */
    public static void seedIfEmpty() {
        try {
            Connection conn = ConnectionManager.getInstance().getConnection();
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM STANZA");
                rs.next();
                int count = rs.getInt(1);
                if (count < 3) {
                    insertDemoData(conn);
                    System.out.println("[SEED] Dati dimostrativi caricati nel database (" + (7 - count) + " stanze aggiunte).");
                }
            }
        } catch (SQLException e) {
            System.err.println("[SEED] Impossibile inserire i dati demo: " + e.getMessage());
        }
    }

    private static void insertDemoData(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // -- Sedi --
            stmt.execute("INSERT INTO SEDE (id, nome, citta) VALUES "
                    + "('FI01', 'EscapeManager Firenze Centro', 'Firenze') "
                    + "ON CONFLICT (id) DO NOTHING");
            stmt.execute("INSERT INTO SEDE (id, nome, citta) VALUES "
                    + "('MI01', 'EscapeManager Milano Navigli', 'Milano') "
                    + "ON CONFLICT (id) DO NOTHING");

            // -- Stanze Firenze (4 stanze) --
            stmt.execute("INSERT INTO STANZA (id, sede_id, tema, capienza_max, prezzo_base, stato_corrente, pricing_strategy) VALUES "
                    + "('R01', 'FI01', 'Horror Asylum',        6, 20.0, 'DISPONIBILE',    'BASE'),"
                    + "('R02', 'FI01', 'Mistero Egizio',       8, 25.0, 'DISPONIBILE',    'WEEKEND'),"
                    + "('R03', 'FI01', 'Fuga dal Laboratorio', 4, 18.0, 'IN_MANUTENZIONE','BASE'),"
                    + "('R04', 'FI01', 'Pirati dei Caraibi',   6, 22.0, 'DISPONIBILE',    'BASE') "
                    + "ON CONFLICT (id) DO NOTHING");

            // -- Stanze Milano (3 stanze) --
            stmt.execute("INSERT INTO STANZA (id, sede_id, tema, capienza_max, prezzo_base, stato_corrente, pricing_strategy) VALUES "
                    + "('R05', 'MI01', 'Spazio Profondo',      5, 28.0, 'DISPONIBILE',    'WEEKEND'),"
                    + "('R06', 'MI01', 'Il Mago di Oz',        7, 20.0, 'DISPONIBILE',    'BASE'),"
                    + "('R07', 'MI01', 'Prison Break',         4, 24.0, 'DISPONIBILE',    'BASE') "
                    + "ON CONFLICT (id) DO NOTHING");

            // -- Utenti demo --
            stmt.execute("INSERT INTO UTENTE (id, email, password_hash, ruolo, nome, telefono) VALUES "
                    + "('C01', 'mario.rossi@email.it',    'hash_demo', 'CLIENTE',     'Mario Rossi',     '3331234567'),"
                    + "('C02', 'luigi.verdi@email.it',    'hash_demo', 'CLIENTE',     'Luigi Verdi',     '3339876543'),"
                    + "('C03', 'anna.bianchi@email.it',   'hash_demo', 'CLIENTE',     'Anna Bianchi',    '3351112233'),"
                    + "('GM01','gm.firenze@escape.it',    'hash_demo', 'GAME_MASTER', 'Luca Neri',       NULL),"
                    + "('A01', 'admin@escapemanager.it',  'hash_demo', 'ADMIN',       'Sara Conti',      NULL) "
                    + "ON CONFLICT (id) DO NOTHING");
        }
    }
}
