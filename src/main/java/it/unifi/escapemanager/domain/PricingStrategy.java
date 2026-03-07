package it.unifi.escapemanager.domain;

public interface PricingStrategy {
    double calcola(double prezzoBase, int numeroGiocatori);
}