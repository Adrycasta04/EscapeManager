package it.unifi.escapemanager.domain;

public class Cliente extends Utente {
    private String nome;
    private String telefono;

    public Cliente(String id, String email, String passwordHash, String nome, String telefono) {
        super(id, email, passwordHash, "CLIENTE"); // Forza il ruolo a CLIENTE
        this.nome = nome;
        this.telefono = telefono;
    }

    public String getNome() { return nome; }
    public String getTelefono() { return telefono; }
}
