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

    // Costruttore usato dal Builder
    private Prenotazione() {}

    // Costruttore diretto (backward-compatible)
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

    // --- Builder Pattern ---
    // Evita costruttori con molti parametri e migliora la leggibilità
    public static class Builder {
        private String id;
        private String clienteId;
        private String stanzaId;
        private LocalDateTime dataOra;
        private int numeroGiocatori;
        private double prezzoTotale;
        private String statoPartita;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder clienteId(String clienteId) {
            this.clienteId = clienteId;
            return this;
        }

        public Builder stanzaId(String stanzaId) {
            this.stanzaId = stanzaId;
            return this;
        }

        public Builder dataOra(LocalDateTime dataOra) {
            this.dataOra = dataOra;
            return this;
        }

        public Builder numeroGiocatori(int numeroGiocatori) {
            this.numeroGiocatori = numeroGiocatori;
            return this;
        }

        public Builder prezzoTotale(double prezzoTotale) {
            this.prezzoTotale = prezzoTotale;
            return this;
        }

        public Builder statoPartita(String statoPartita) {
            this.statoPartita = statoPartita;
            return this;
        }

        public Prenotazione build() {
            Prenotazione p = new Prenotazione();
            p.id = this.id;
            p.clienteId = this.clienteId;
            p.stanzaId = this.stanzaId;
            p.dataOra = this.dataOra;
            p.numeroGiocatori = this.numeroGiocatori;
            p.prezzoTotale = this.prezzoTotale;
            p.statoPartita = this.statoPartita;
            return p;
        }
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
