package it.unifi.escapemanager.cli;

public class Main {
    public static void main(String[] args) {
        // Popola il database con dati demo se le tabelle sono vuote
        DemoDataSeeder.seedIfEmpty();

        ConsoleMenu menu = new ConsoleMenu();
        menu.start();
    }
}