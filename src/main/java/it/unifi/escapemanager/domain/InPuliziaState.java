package it.unifi.escapemanager.domain;

public class InPuliziaState implements RoomState {
    @Override
    public void prenota(Stanza stanza) {
        throw new IllegalStateException("Impossibile prenotare: Stanza in pulizia.");
    }

    @Override
    public void iniziaSessione(Stanza stanza) {
        throw new IllegalStateException("Impossibile avviare: Stanza in pulizia.");
    }

    @Override
    public void terminaSessione(Stanza stanza) {
        throw new IllegalStateException("Nessuna sessione attiva.");
    }

    @Override
    public void setManutenzione(Stanza stanza) {
        stanza.setStato(new InManutenzioneState());
    }

    @Override
    public void setDisponibile(Stanza stanza) {
        stanza.setStato(new DisponibileState());
    }

    @Override
    public String getStatusString() {
        return "IN_PULIZIA";
    }
}
