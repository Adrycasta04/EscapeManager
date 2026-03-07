package it.unifi.escapemanager.domain;

public class Stanza {
    private String id;
    private String sedeId;
    private String tema;
    private int capienzaMax;
    private double prezzoBase;
    
    // Pattern references
    private RoomState statoCorrente;
    private PricingStrategy pricingStrategy; // L'interfaccia per lo Strategy Pattern

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

    public String getId() { return id; }
    public String getTema() { return tema; }
    public String getStatoString() { return statoCorrente.getStatusString(); }
    
    public String getSedeId() { return sedeId; }
    public int getCapienzaMax() { return capienzaMax; }
    public double getPrezzoBase() { return prezzoBase; }
    
}
