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
import java.time.format.DateTimeFormatter;
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

    private static final String SEPARATORE = "-------------------------------------------------------";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

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
        stampaIntestazione();
        while (true) {
            System.out.println("\n" + SEPARATORE);
            System.out.println("  MENU PRINCIPALE");
            System.out.println(SEPARATORE);
            System.out.println("  1. Visualizza Catalogo Stanze");
            System.out.println("  2. Prenota una Stanza         (UC1)");
            System.out.println("  3. Aggiorna Stato Stanza       (UC3 - Game Master)");
            System.out.println("  4. Configura Tariffe           (UC4 - Admin)");
            System.out.println("  0. Esci");
            System.out.println(SEPARATORE);
            System.out.print("  Seleziona: ");

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
                    System.out.println("\n  Uscita dal sistema. Arrivederci!");
                    return;
                default:
                    System.out.println("  [!] Opzione non valida. Riprova.");
            }
        }
    }

    // ────────────── CATALOGO ──────────────

    private void mostraCatalogo() {
        System.out.println("\n" + SEPARATORE);
        System.out.println("  CATALOGO STANZE");
        System.out.println(SEPARATORE);
        try {
            List<Stanza> stanze = stanzaDAO.findAll();
            if (stanze.isEmpty()) {
                System.out.println("  (nessuna stanza presente nel database)");
                return;
            }
            System.out.printf("  %-5s %-22s %6s %10s  %-15s%n",
                    "ID", "Tema", "Max", "Prezzo", "Stato");
            System.out.println("  " + "-----------------------------------------------------");
            for (Stanza s : stanze) {
                System.out.printf("  %-5s %-22s %4d   EUR %5.2f  %-15s%n",
                        s.getId(),
                        s.getTema(),
                        s.getCapienzaMax(),
                        s.getPrezzoBase(),
                        s.getStatoString());
            }
        } catch (EscapeManagerException e) {
            System.out.println("  [ERRORE] " + e.getMessage());
        }
    }

    // ────────────── UC1: PRENOTAZIONE ──────────────

    private void gestisciPrenotazione() {
        System.out.println("\n" + SEPARATORE);
        System.out.println("  NUOVA PRENOTAZIONE (UC1)");
        System.out.println(SEPARATORE);
        mostraCatalogo();

        System.out.print("\n  Inserisci il tuo ID Cliente (es. C01): ");
        String clienteId = scanner.nextLine().trim();
        if (clienteId.isEmpty()) {
            System.out.println("  [!] ID Cliente obbligatorio.");
            return;
        }

        System.out.print("  Inserisci l'ID della Stanza da prenotare (es. R01): ");
        String stanzaId = scanner.nextLine().trim().toUpperCase();
        if (stanzaId.isEmpty()) {
            System.out.println("  [!] ID Stanza obbligatorio.");
            return;
        }

        System.out.print("  Numero di giocatori: ");
        int numeroGiocatori;
        try {
            numeroGiocatori = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  [ERRORE] Inserisci un numero intero valido.");
            return;
        }

        LocalDate dataSelezionata;
        LocalTime oraSelezionata;
        try {
            System.out.printf("  Data della prenotazione (DD/MM/YYYY) [Invio = %s]: ",
                    LocalDate.now().format(DATE_FMT));
            String dataInput = scanner.nextLine().trim();
            if (dataInput.isEmpty()) {
                dataSelezionata = LocalDate.now();
            } else {
                dataSelezionata = LocalDate.parse(dataInput, DATE_FMT);
            }

            System.out.print("  Orario (HH:MM) [Invio = 21:00]: ");
            String oraInput = scanner.nextLine().trim();
            if (oraInput.isEmpty()) {
                oraSelezionata = LocalTime.of(21, 0);
            } else {
                oraSelezionata = LocalTime.parse(oraInput, TIME_FMT);
            }
        } catch (DateTimeParseException e) {
            System.out.println("  [ERRORE] Formato data o orario non valido.");
            return;
        }

        LocalDateTime slot = LocalDateTime.of(dataSelezionata, oraSelezionata);

        try {
            Prenotazione p = prenotazioneController.effettuaPrenotazione(clienteId, stanzaId, slot, numeroGiocatori);
            System.out.println("\n  " + "========================================");
            System.out.println("  [OK] PRENOTAZIONE CONFERMATA!");
            System.out.println("  " + "========================================");
            System.out.println("  ID Prenotazione : " + p.getId().substring(0, 8) + "...");
            System.out.println("  Data e Ora      : " + slot.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            System.out.println("  Giocatori       : " + p.getNumeroGiocatori());
            System.out.println("  Prezzo Totale   : EUR " + String.format("%.2f", p.getPrezzoTotale()));
            System.out.println("  Stato           : " + p.getStatoPartita());
        } catch (SlotNonDisponibileException e) {
            System.out.println("\n  [!] " + e.getMessage());
            System.out.print("  Vuoi iscriverti alla lista d'attesa? (S/N): ");
            String risposta = scanner.nextLine().trim().toUpperCase();
            if ("S".equals(risposta)) {
                try {
                    listaAttesaController.iscrivi(clienteId, stanzaId);
                    System.out.println("  [OK] Iscrizione alla lista d'attesa completata.");
                } catch (EscapeManagerException ex) {
                    System.out.println("  [ERRORE] " + ex.getMessage());
                }
            }
        } catch (EscapeManagerException e) {
            System.out.println("  [ERRORE] " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("  [ERRORE] " + e.getMessage());
        }
    }

    // ────────────── UC3: GESTIONE STATO ──────────────

    private void gestisciStatoStanza() {
        System.out.println("\n" + SEPARATORE);
        System.out.println("  AGGIORNA STATO STANZA (UC3 - Game Master)");
        System.out.println(SEPARATORE);
        mostraCatalogo();

        System.out.print("\n  ID Stanza da aggiornare: ");
        String stanzaId = scanner.nextLine().trim().toUpperCase();
        if (stanzaId.isEmpty()) {
            System.out.println("  [!] ID Stanza obbligatorio.");
            return;
        }

        System.out.println("  Comandi disponibili:");
        System.out.println("    1. AVVIA        -> Disponibile -> In Corso");
        System.out.println("    2. TERMINA      -> In Corso -> In Pulizia");
        System.out.println("    3. PULISCI      -> In Pulizia -> Disponibile");
        System.out.println("    4. MANUTENZIONE -> Qualsiasi -> In Manutenzione");
        System.out.print("  Comando (o numero): ");
        String comandoInput = scanner.nextLine().trim().toUpperCase();
        String comando = normalizzaComando(comandoInput);

        try {
            gestioneStatoController.eseguiTransizione(stanzaId, comando);
            // Rileggiamo lo stato aggiornato dal DB
            Stanza aggiornata = stanzaDAO.findById(stanzaId);
            System.out.println("\n  [OK] Transizione eseguita con successo.");
            if (aggiornata != null) {
                System.out.println("  Nuovo stato di " + aggiornata.getTema() + ": " + aggiornata.getStatoString());
            }
        } catch (EscapeManagerException e) {
            System.out.println("  [ERRORE] " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("  [ERRORE] " + e.getMessage());
        }
    }

    // ────────────── UC4: TARIFFE ──────────────

    private void gestisciTariffe() {
        System.out.println("\n" + SEPARATORE);
        System.out.println("  CONFIGURA TARIFFE (UC4 - Admin)");
        System.out.println(SEPARATORE);
        mostraCatalogo();

        System.out.print("\n  ID Stanza da configurare: ");
        String stanzaId = scanner.nextLine().trim().toUpperCase();
        if (stanzaId.isEmpty()) {
            System.out.println("  [!] ID Stanza obbligatorio.");
            return;
        }

        System.out.println("  Strategie di prezzo disponibili:");
        System.out.println("    BASE    -> Prezzo base * numero giocatori");
        System.out.println("    WEEKEND -> Prezzo base * numero giocatori * 1.20 (+20%)");
        System.out.print("  Nuova tariffa: ");
        String tariffa = scanner.nextLine().trim().toUpperCase();

        try {
            tariffeController.cambiaTariffa(stanzaId, tariffa);
            System.out.println("\n  [OK] Strategia di prezzo aggiornata a: " + tariffa);
        } catch (EscapeManagerException e) {
            System.out.println("  [ERRORE] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  [ERRORE] " + e.getMessage());
        }
    }

    // ────────────── UTILITY ──────────────

    private void stampaIntestazione() {
        System.out.println();
        System.out.println("  +===================================================+");
        System.out.println("  |           ESCAPE MANAGER - CLI v1.0              |");
        System.out.println("  |     Sistema Gestionale per Escape Room           |");
        System.out.println("  +===================================================+");
    }

    /**
     * Normalizza varianti naturali dei comandi di transizione stato.
     * Gestisce input con spazi, abbreviazioni e selezione numerica.
     */
    private String normalizzaComando(String input) {
        // Mapping numerico
        switch (input) {
            case "1": return "AVVIA";
            case "2": return "TERMINA";
            case "3": return "PULISCI";
            case "4": return "MANUTENZIONE";
        }
        // Rimozione parole superflue per varianti naturali
        String normalizzato = input
                .replace("IN ", "")
                .replace("SESSIONE", "")
                .replace("IMPOSTA ", "")
                .trim();
        // Mapping alias comuni
        if (normalizzato.startsWith("MANUT")) return "MANUTENZIONE";
        if (normalizzato.startsWith("AVVI"))  return "AVVIA";
        if (normalizzato.startsWith("TERM"))  return "TERMINA";
        if (normalizzato.startsWith("PULI"))  return "PULISCI";
        return normalizzato;
    }
}
