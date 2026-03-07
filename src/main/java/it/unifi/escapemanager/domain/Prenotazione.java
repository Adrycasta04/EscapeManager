package it.unifi.escapemanager.domain;

import java.time.LocalDateTime;

public class Prenotazione {
    private String id;
    private String clienteId;
    private String stanzaId;
    private LocalDateTime dataOra;
    private int numeroGiocatori;
    private double prezzoTotale;
    private String statoPartita; // Es: "CONFERMATA", "CONCLUSA", "ANNULLATA"

    public Prenotazione(String id, String clienteId, String stanzaId, LocalDateTime dataOra, 
                        int numeroGiocatori, double prezzoTotale, String statoPartita) {
        this.id = id;
        this.clienteId = clienteId;
        this.stanzaId = stanzaId;
        this.dataOra = dataOra;
        this.numeroGiocatori = numeroGiocatori;
        this.prezzoTotale = prezzoTotale;
        this.statoPartita = statoPartita;
    }

    // Getters
    public String getId() { return id; }
    public String getClienteId() { return clienteId; }
    public String getStanzaId() { return stanzaId; }
    public LocalDateTime getDataOra() { return dataOra; }
    public int getNumeroGiocatori() { return numeroGiocatori; }
    public double getPrezzoTotale() { return prezzoTotale; }
    public String getStatoPartita() { return statoPartita; }

    // Setters
    public void setStatoPartita(String statoPartita) {
        this.statoPartita = statoPartita;
    }
}
