package it.unifi.escapemanager.domain;

public class DisponibileState implements RoomState {

    @Override
    public void prenota(Stanza stanza) {
        // La logica di prenotazione vera e propria (es. controllo orari) sarà nel Controller.
        // Qui ci occupiamo solo del ciclo di vita.
    }

    @Override
    public void iniziaSessione(Stanza stanza) {
        stanza.setStato(new InCorsoState());
    }

    @Override
    public void terminaSessione(Stanza stanza) {
        throw new IllegalStateException("Impossibile terminare una sessione per una stanza DISPONIBILE.");
    }

    @Override
    public void setManutenzione(Stanza stanza) {
        stanza.setStato(new InManutenzioneState());
    }

    @Override
    public void setDisponibile(Stanza stanza) {
        throw new IllegalStateException("La stanza è già disponibile.");
    }

    @Override
    public String getStatusString() {
        return "DISPONIBILE";
    }
}