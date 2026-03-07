package it.unifi.escapemanager.domain;

public class TariffaBase implements PricingStrategy {
    @Override
    public double calcola(double prezzoBase, int numeroGiocatori) {
        // La logica base: prezzo per singola persona moltiplicato per i giocatori
        return prezzoBase * numeroGiocatori; 
    }
}