package it.unifi.escapemanager.domain;

import java.util.Objects;

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

    // --- Java Idioms: equals, hashCode, toString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return Objects.equals(id, utente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id='" + id + "', email='" + email + "', ruolo='" + ruolo + "'}";
    }
}