package it.unifi.escapemanager.domain;

public class InPuliziaState implements RoomState {
    @Override
    public void prenota(Stanza stanza) {
        System.out.println("Prenotazione accettata per gli slot successivi.");
    }

    @Override
    public void iniziaSessione(Stanza stanza) {
        throw new IllegalStateException("Attendi, la stanza è in fase di ripristino/pulizia.");
    }

    @Override
    public void terminaSessione(Stanza stanza) {
        throw new IllegalStateException("Nessuna sessione da terminare.");
    }

    @Override
    public void setManutenzione(Stanza stanza) {
        stanza.setStato(new InManutenzioneState());
    }

    @Override
    public String getStatusString() {
        return "IN_PULIZIA";
    }
}
