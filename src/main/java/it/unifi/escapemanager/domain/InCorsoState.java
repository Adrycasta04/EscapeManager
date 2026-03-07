package it.unifi.escapemanager.domain;

public class InCorsoState implements RoomState {
    @Override
    public void prenota(Stanza stanza) {
        throw new IllegalStateException("Impossibile prenotare per questo slot: la stanza è attualmente in corso.");
    }

    @Override
    public void iniziaSessione(Stanza stanza) {
        throw new IllegalStateException("La sessione è già in corso.");
    }

    @Override
    public void terminaSessione(Stanza stanza) {
        System.out.println("Sessione terminata. La stanza passa in IN_PULIZIA.");
        stanza.setStato(new InPuliziaState()); // Transizione automatica
    }

    @Override
    public void setManutenzione(Stanza stanza) {
        System.out.println("Guasto durante il gioco! Partita interrotta, stanza in MANUTENZIONE.");
        stanza.setStato(new InManutenzioneState());
    }

    @Override
    public String getStatusString() {
        return "IN_CORSO";
    }
}