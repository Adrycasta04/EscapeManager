package it.unifi.escapemanager.domain;

public abstract class Utente {
    protected String id;
    protected String email;
    protected String passwordHash;
    protected String ruolo; // 'CLIENTE', 'GAME_MASTER', 'ADMIN'

    public Utente(String id, String email, String passwordHash, String ruolo) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.ruolo = ruolo;
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRuolo() { return ruolo; }
}