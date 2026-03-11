package it.unifi.escapemanager.cli;

import it.unifi.escapemanager.controllers.GestioneStatoController;
import it.unifi.escapemanager.controllers.PrenotazioneController;
import it.unifi.escapemanager.controllers.TariffeController;
import it.unifi.escapemanager.dao.DAOFactory;
import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.Prenotazione;
import it.unifi.escapemanager.domain.Stanza;
import it.unifi.escapemanager.exceptions.EscapeManagerException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final PrenotazioneController prenotazioneController;
    private final GestioneStatoController gestioneStatoController;
    private final TariffeController tariffeController;
    private final StanzaDAO stanzaDAO;
    private final Scanner scanner;

    public ConsoleMenu() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.stanzaDAO = factory.getStanzaDAO();
        this.prenotazioneController = new PrenotazioneController(factory.getPrenotazioneDAO(), stanzaDAO);
        this.gestioneStatoController = new GestioneStatoController();
        this.tariffeController = new TariffeController();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== ESCAPE MANAGER CLI ===");
            System.out.println("1. Cliente: Ricerca Stanze (UC1)");
            System.out.println("2. Cliente: Prenota una Stanza (UC2)");
            System.out.println("3. Game Master: Aggiorna Stato Stanza (UC3)");
            System.out.println("4. Admin: Configura Tariffe (UC4)");
            System.out.println("0. Esci");
            System.out.print("Seleziona un'opzione: ");

            String scelta = scanner.nextLine().trim();

            switch (scelta) {
                case "1":
                    mostraCatalogo();
                    break;
                case "2":
                    gestisciPrenotazione();
                    break;
                case "3":
                    gestisciStatoStanza();
                    break;
                case "4":
                    gestisciTariffe();
                    break;
                case "0":
                    System.out.println("Uscita dal sistema...");
                    return;
                default:
                    System.out.println("Opzione non valida. Riprova.");
            }
        }
    }

    // ---------- Caso 2: Prenotazione (UC2) ----------

    private void gestisciPrenotazione() {
        System.out.println("\n--- NUOVA PRENOTAZIONE (UC2) ---");
        mostraCatalogo();

        System.out.print("ID Cliente: ");
        String clienteId = scanner.nextLine().trim();

        System.out.print("ID Stanza (es. R01): ");
        String stanzaId = scanner.nextLine().trim().toUpperCase();

        System.out.print("Numero di giocatori: ");
        int numeroGiocatori;
        try {
            numeroGiocatori = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[ERRORE] Inserisci un numero intero valido.");
            return;
        }

        // Slot simulato: oggi alle 21:00
        LocalDateTime slot = LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 0));

        try {
            Prenotazione p = prenotazioneController.effettuaPrenotazione(clienteId, stanzaId, slot, numeroGiocatori);
            System.out.println("\n[OK] PRENOTAZIONE CONFERMATA!");
            System.out.println("  ID Prenotazione: " + p.getId());
            System.out.println("  Prezzo Totale: EUR " + p.getPrezzoTotale());
            System.out.println("  Stato: " + p.getStatoPartita());
        } catch (EscapeManagerException e) {
            System.out.println("[ERRORE] " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }

    // ---------- Caso 2: Game Master (UC3) ----------

    private void gestisciStatoStanza() {
        System.out.println("\n--- AGGIORNA STATO STANZA (UC3) ---");
        mostraCatalogo();

        System.out.print("ID Stanza: ");
        String stanzaId = scanner.nextLine().trim().toUpperCase();

        System.out.println("Comandi: AVVIA | TERMINA | MANUTENZIONE | PULISCI");
        System.out.print("Comando: ");
        String comando = scanner.nextLine().trim().toUpperCase();

        try {
            gestioneStatoController.eseguiTransizione(stanzaId, comando);
            System.out.println("[OK] Transizione eseguita con successo.");
        } catch (EscapeManagerException e) {
            System.out.println("[ERRORE] " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }

    // ---------- Caso 3: Admin (UC4) ----------

    private void gestisciTariffe() {
        System.out.println("\n--- CONFIGURA TARIFFE (UC4) ---");
        mostraCatalogo();

        System.out.print("ID Stanza: ");
        String stanzaId = scanner.nextLine().trim().toUpperCase();

        System.out.println("Tariffe disponibili: BASE | WEEKEND");
        System.out.print("Tariffa: ");
        String tariffa = scanner.nextLine().trim().toUpperCase();

        try {
            tariffeController.cambiaTariffa(stanzaId, tariffa);
            System.out.println("[OK] Tariffa aggiornata con successo.");
        } catch (EscapeManagerException e) {
            System.out.println("[ERRORE] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }

    // ---------- Caso 1: Ricerca Stanze (UC1) ----------

    private void mostraCatalogo() {
        System.out.println("\n--- CATALOGO STANZE (UC1) ---");
        try {
            List<Stanza> stanze = stanzaDAO.findAll();
            if (stanze.isEmpty()) {
                System.out.println("  (nessuna stanza presente)");
                return;
            }
            for (Stanza s : stanze) {
                System.out.printf("  [%s] %s - Max %d giocatori - EUR %.2f - Stato: %s%n",
                        s.getId(), s.getTema(), s.getCapienzaMax(), s.getPrezzoBase(), s.getStatoString());
            }
        } catch (EscapeManagerException e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }
}