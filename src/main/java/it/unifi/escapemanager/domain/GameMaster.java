package it.unifi.escapemanager.domain;

public class GameMaster extends Utente {
    private String matricola;

    public GameMaster(String id, String email, String passwordHash, String matricola) {
        super(id, email, passwordHash, "GAME_MASTER"); // Forza il ruolo a GAME_MASTER
        this.matricola = matricola;
    }

    public String getMatricola() { return matricola; }
}