package it.unifi.escapemanager.cli;

import it.unifi.escapemanager.controllers.GestioneStatoController;
import it.unifi.escapemanager.controllers.ListaAttesaController;
import it.unifi.escapemanager.controllers.PrenotazioneController;
import it.unifi.escapemanager.controllers.TariffeController;
import it.unifi.escapemanager.dao.DAOFactory;
import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.Prenotazione;
import it.unifi.escapemanager.domain.Stanza;
import it.unifi.escapemanager.exceptions.EscapeManagerException;
import it.unifi.escapemanager.exceptions.SlotNonDisponibileException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final PrenotazioneController prenotazioneController;
    private final GestioneStatoController gestioneStatoController;
    private final TariffeController tariffeController;
    private final ListaAttesaController listaAttesaController;
    private final StanzaDAO stanzaDAO;
    private final Scanner scanner;

    public ConsoleMenu() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.stanzaDAO = factory.getStanzaDAO();
        this.prenotazioneController = new PrenotazioneController(factory.getPrenotazioneDAO(), stanzaDAO);
        this.gestioneStatoController = new GestioneStatoController(stanzaDAO);
        this.tariffeController = new TariffeController(stanzaDAO);
        this.listaAttesaController = new ListaAttesaController(factory.getWaitingListDAO());
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== ESCAPE MANAGER CLI ===");
            System.out.println("1. Cliente: Catalogo Stanze (supporto UC1)");
            System.out.println("2. Cliente: Prenota una Stanza (UC1)");
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

    // ---------- Caso 1: Prenotazione (UC1) ----------

    private void gestisciPrenotazione() {
        System.out.println("\n--- NUOVA PRENOTAZIONE (UC1) ---");
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

        LocalDate dataSelezionata;
        LocalTime oraSelezionata;
        try {
            System.out.print("Data (YYYY-MM-DD, vuoto = oggi): ");
            String dataInput = scanner.nextLine().trim();
            dataSelezionata = dataInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dataInput);

            System.out.print("Ora (HH:MM, vuoto = 21:00): ");
            String oraInput = scanner.nextLine().trim();
            oraSelezionata = oraInput.isEmpty() ? LocalTime.of(21, 0) : LocalTime.parse(oraInput);
        } catch (DateTimeParseException e) {
            System.out.println("[ERRORE] Formato data/ora non valido.");
            return;
        }

        LocalDateTime slot = LocalDateTime.of(dataSelezionata, oraSelezionata);

        try {
            Prenotazione p = prenotazioneController.effettuaPrenotazione(clienteId, stanzaId, slot, numeroGiocatori);
            System.out.println("\n[OK] PRENOTAZIONE CONFERMATA!");
            System.out.println("  ID Prenotazione: " + p.getId());
            System.out.println("  Prezzo Totale: EUR " + p.getPrezzoTotale());
            System.out.println("  Stato: " + p.getStatoPartita());
        } catch (SlotNonDisponibileException e) {
            System.out.println("[ERRORE] " + e.getMessage());
            System.out.print("Vuoi iscriverti alla lista d'attesa? (S/N): ");
            String risposta = scanner.nextLine().trim().toUpperCase();
            if ("S".equals(risposta)) {
                try {
                    listaAttesaController.iscrivi(clienteId, stanzaId);
                    System.out.println("[OK] Iscrizione alla lista d'attesa completata.");
                } catch (EscapeManagerException ex) {
                    System.out.println("[ERRORE] " + ex.getMessage());
                }
            }
        } catch (EscapeManagerException e) {
            System.out.println("[ERRORE] " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[ERRORE] " + e.getMessage());
        }
    }

    // ---------- Caso 3: Game Master (UC3) ----------

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

    // ---------- Caso 4: Admin (UC4) ----------

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
