package it.unifi.escapemanager.domain;

public class DisponibileState implements RoomState {

    @Override
    public void prenota(Stanza stanza) {
        // La logica di prenotazione vera e propria (es. controllo orari) sarà nel Controller.
        // Qui ci occupiamo solo del ciclo di vita.
        System.out.println("Prenotazione accettata per la stanza: " + stanza.getTema());
    }

    @Override
    public void iniziaSessione(Stanza stanza) {
        System.out.println("Sessione avviata. La stanza passa in stato IN CORSO.");
        // Transizione di stato!
        stanza.setStato(new InCorsoState()); 
    }

    @Override
    public void terminaSessione(Stanza stanza) {
        throw new IllegalStateException("Impossibile terminare una sessione per una stanza DISPONIBILE.");
    }

    @Override
    public void setManutenzione(Stanza stanza) {
        System.out.println("Stanza messa in MANUTENZIONE.");
        stanza.setStato(new InManutenzioneState());
    }

    @Override
    public String getStatusString() {
        return "DISPONIBILE";
    }
}