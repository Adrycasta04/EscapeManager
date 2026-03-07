package it.unifi.escapemanager.domain;

public class InManutenzioneState implements RoomState {
    @Override
    public void prenota(Stanza stanza) {
        throw new IllegalStateException("Impossibile prenotare: Stanza fuori servizio.");
    }

    @Override
    public void iniziaSessione(Stanza stanza) {
        throw new IllegalStateException("Impossibile avviare: Stanza fuori servizio.");
    }

    @Override
    public void terminaSessione(Stanza stanza) {
        throw new IllegalStateException("Nessuna partita in corso.");
    }

    @Override
    public void setManutenzione(Stanza stanza) {
        // Già in manutenzione, non fa nulla
    }

    @Override
    public void setDisponibile(Stanza stanza) {
        stanza.setStato(new DisponibileState());
    }

    @Override
    public String getStatusString() {
        return "IN_MANUTENZIONE";
    }
}