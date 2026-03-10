package it.unifi.escapemanager.cli;

import it.unifi.escapemanager.controllers.PrenotazioneController;
import it.unifi.escapemanager.dao.StanzaDAO;
import it.unifi.escapemanager.domain.Prenotazione;
import it.unifi.escapemanager.domain.Stanza;
import it.unifi.escapemanager.exceptions.DatabaseException;
import it.unifi.escapemanager.exceptions.SlotNonDisponibileException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final PrenotazioneController prenotazioneController;
    private final StanzaDAO stanzaDAO;
    private final Scanner scanner;

    public ConsoleMenu(PrenotazioneController prenotazioneController, StanzaDAO stanzaDAO) {
        this.prenotazioneController = prenotazioneController;
        this.stanzaDAO = stanzaDAO;
        this.scanner = new Scanner(System.in);
    }

    public void avvia() {
        System.out.println("=========================================");
        System.out.println("   BENVENUTO IN ESCAPEMANAGER (CLI)      ");
        System.out.println("=========================================");
        
        // Simuliamo un login per testare la prenotazione (Hardcoded per il prototipo)
        String clienteIdLoggato = "CLIENTE_TEST_01"; 
        boolean inEsecuzione = true;

        while (inEsecuzione) {
            System.out.println("\nScegli un'operazione:");
            System.out.println("1. Visualizza Catalogo Stanze");
            System.out.println("2. Effettua una Prenotazione (UC2)");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");
            
            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    mostraCatalogo();
                    break;
                case "2":
                    gestisciPrenotazione(clienteIdLoggato);
                    break;
                case "0":
                    System.out.println("Chiusura del sistema... Arrivederci!");
                    inEsecuzione = false;
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }

    private void mostraCatalogo() {
        try {
            System.out.println("\n--- CATALOGO STANZE ---");
            List<Stanza> stanze = stanzaDAO.findAll();
            if (stanze.isEmpty()) {
                System.out.println("Nessuna stanza presente nel database.");
                return;
            }
            for (Stanza s : stanze) {
                System.out.printf("[%s] %s - Max %d giocatori - Stato: %s%n", 
                    s.getId(), s.getTema(), s.getCapienzaMax(), s.getStatoString());
            }
        } catch (DatabaseException e) {
            System.err.println("Errore di lettura dal database: " + e.getMessage());
        }
    }

    private void gestisciPrenotazione(String clienteId) {
        System.out.println("\n--- NUOVA PRENOTAZIONE ---");
        System.out.print("Inserisci l'ID della stanza da prenotare (es. 'R01'): ");
        String stanzaId = scanner.nextLine().trim().toUpperCase().replace("'", "");

        System.out.print("Inserisci la data e l'ora (Formato: YYYY-MM-DD HH:MM, es. 2026-10-31 21:00): ");
        String dataInput = scanner.nextLine();

        System.out.print("Inserisci il numero di giocatori: ");
        int numeroGiocatori;
        try {
            numeroGiocatori = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Errore: Inserisci un numero intero valido.");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dataOra = LocalDateTime.parse(dataInput, formatter);

            // Chiamata al Controller (La logica di Business!)
            Prenotazione p = prenotazioneController.effettuaPrenotazione(clienteId, stanzaId, dataOra, numeroGiocatori);
            
            System.out.println("\n[OK] PRENOTAZIONE CONFERMATA!");
            System.out.println("ID Prenotazione: " + p.getId());
            System.out.println("Prezzo Totale Calcolato: EUR " + p.getPrezzoTotale());
            System.out.println("Stato: " + p.getStatoPartita());

        } catch (SlotNonDisponibileException e) {
            // Qui gestiamo l'eccezione custom che avevamo creato!
            System.err.println("\n ERRORE: " + e.getMessage());
            System.out.println("Vuoi iscriverti alla Lista d'Attesa per questo slot? (S/N)");
            String risposta = scanner.nextLine(); 
            if (risposta.equalsIgnoreCase("S")) {
                System.out.println(" [OK] Sei stato aggiunto agli Observer di questa Stanza!");
            }      

        }  catch (IllegalArgumentException e) {
            System.err.println("\n ERRORE DI VALIDAZIONE: " + e.getMessage());

        } catch (IllegalStateException e) {
            // Questo cattura gli errori dello State Pattern (es. Stanza in manutenzione)
            System.err.println("\n ERRORE STATO STANZA: " + e.getMessage());

        } catch (Exception e) {
            System.err.println("\n ERRORE DI SISTEMA: Si è verificato un problema imprevisto.");
            e.printStackTrace();
        }
    }
}