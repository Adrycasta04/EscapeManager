package it.unifi.escapemanager.cli;

import it.unifi.escapemanager.controllers.PrenotazioneController;
import it.unifi.escapemanager.dao.DAOFactory;
import it.unifi.escapemanager.dao.PrenotazioneDAO;
import it.unifi.escapemanager.dao.StanzaDAO;

public class Main {
    public static void main(String[] args) {
        
        // 1. Inizializzazione dello Strato di Persistenza (DAOs) tramite Factory
        DAOFactory factory = DAOFactory.getDAOFactory();
        StanzaDAO stanzaDAO = factory.getStanzaDAO();
        PrenotazioneDAO prenotazioneDAO = factory.getPrenotazioneDAO();

        // 2. Inizializzazione dello Strato di Business Logic (Controllers)
        PrenotazioneController prenotazioneController = new PrenotazioneController(prenotazioneDAO, stanzaDAO);

        // 3. Inizializzazione e avvio dello Strato di Presentazione (View/CLI)
        ConsoleMenu menu = new ConsoleMenu(prenotazioneController, stanzaDAO);
        
        // Let's go!
        menu.avvia();
    }
}