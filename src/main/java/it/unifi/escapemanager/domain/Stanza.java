package it.unifi.escapemanager.domain;

import java.util.Objects;

public class Stanza {
    private String id;
    private String sedeId;
    private String tema;
    private int capienzaMax;
    private double prezzoBase;
    
    // Pattern references
    private RoomState statoCorrente;
    private PricingStrategy pricingStrategy; // L'interfaccia per lo Strategy Pattern
    private final WaitingList listaAttesa = new WaitingList(); // Observer Pattern

    public Stanza(String id, String sedeId, String tema, int capienzaMax, double prezzoBase) {
        this.id = id;
        this.sedeId = sedeId;
        this.tema = tema;
        this.capienzaMax = capienzaMax;
        this.prezzoBase = prezzoBase;
        
        // Stato di default alla creazione
        this.statoCorrente = new DisponibileState(); 
        // Strategia di default
        this.pricingStrategy = new TariffaBase(); 
    }

    // --- Metodi delegati allo State Pattern ---
    
    public void iniziaSessione() {
        this.statoCorrente.iniziaSessione(this);
    }

    // Delega il controllo della prenotazione allo State Pattern!
    public void prenota() {
        this.statoCorrente.prenota(this);
    }

    public void terminaSessione() {
        this.statoCorrente.terminaSessione(this);
    }
    
    public void impostaManutenzione() {
        this.statoCorrente.setManutenzione(this);
    }

    public void impostaDisponibile() {
        this.statoCorrente.setDisponibile(this);
    }

    // --- Metodo delegato allo Strategy Pattern ---
    
    public double calcolaPrezzo(int numeroGiocatori) {
        return this.pricingStrategy.calcola(this.prezzoBase, numeroGiocatori);
    }

    // --- Getters & Setters ---

    public void setStato(RoomState nuovoStato) {
        this.statoCorrente = nuovoStato;
    }
    
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }
    
    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    public String getId() { return id; }
    public String getTema() { return tema; }
    public RoomState getStato() { return statoCorrente; }
    public String getStatoString() { return statoCorrente.getStatusString(); }
    
    public String getSedeId() { return sedeId; }
    public int getCapienzaMax() { return capienzaMax; }
    public double getPrezzoBase() { return prezzoBase; }
    public WaitingList getListaAttesa() { return listaAttesa; }

    // --- Java Idioms: equals, hashCode, toString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stanza stanza = (Stanza) o;
        return Objects.equals(id, stanza.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Stanza{id='" + id + "', tema='" + tema + "', stato=" + getStatoString() + 
               ", prezzoBase=" + prezzoBase + "}";
    }
}
