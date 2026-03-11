package it.unifi.escapemanager.domain;

public class TariffaWeekend implements PricingStrategy {
    @Override
    public double calcola(double prezzoBase, int numeroGiocatori) {
        // Maggiorazione del 20% per il weekend
        return (prezzoBase * numeroGiocatori) * 1.20;
    }
}
