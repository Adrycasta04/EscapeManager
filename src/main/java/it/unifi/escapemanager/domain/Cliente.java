package it.unifi.escapemanager.domain;

import java.time.LocalDateTime;

public class Cliente extends Utente implements WaitingListObserver {
    private String nome;
    private String telefono;
    private String ultimaNotifica;

    public Cliente(String id, String email, String passwordHash, String nome, String telefono) {
        super(id, email, passwordHash, "CLIENTE"); // Forza il ruolo a CLIENTE
        this.nome = nome;
        this.telefono = telefono;
    }

    @Override
    public void update(Stanza stanza, LocalDateTime slotLiberato) {
        // Salva l'ultima notifica ricevuta dalla lista d'attesa
        this.ultimaNotifica = "Slot liberato per stanza '" + stanza.getTema() + "' alle " + slotLiberato;
    }

    public String getNome() { return nome; }
    public String getTelefono() { return telefono; }
    public String getUltimaNotifica() { return ultimaNotifica; }
}
