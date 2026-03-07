package it.unifi.escapemanager.domain;

public class Admin extends Utente {
    private int livelloAccesso;

    public Admin(String id, String email, String passwordHash, int livelloAccesso) {
        super(id, email, passwordHash, "ADMIN"); // Forza il ruolo a ADMIN
        this.livelloAccesso = livelloAccesso;
    }

    public int getLivelloAccesso() { return livelloAccesso; }
}