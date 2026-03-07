package it.unifi.escapemanager.domain;

public class InCorsoState implements RoomState {
    @Override
    public void prenota(Stanza stanza) {
        throw new IllegalStateException("Impossibile prenotare per questo slot: la stanza è attualmente in corso.");
    }

    @Override
    public void iniziaSessione(Stanza stanza) {
        throw new IllegalStateException("Impossibile avviare: la stanza è già in corso.");
    }

    @Override
    public void terminaSessione(Stanza stanza) {
        stanza.setStato(new InPuliziaState());
    }

    @Override
    public void setManutenzione(Stanza stanza) {
        stanza.setStato(new InManutenzioneState());
    }

    @Override
    public void setDisponibile(Stanza stanza) {
        throw new IllegalStateException("Impossibile rendere disponibile: sessione in corso.");
    }

    @Override
    public String getStatusString() {
        return "IN_CORSO";
    }
}