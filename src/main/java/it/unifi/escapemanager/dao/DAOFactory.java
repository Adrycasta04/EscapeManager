package it.unifi.escapemanager.dao;

public interface DAOFactory {

    StanzaDAO getStanzaDAO();

    PrenotazioneDAO getPrenotazioneDAO();
    
    WaitingListDAO getWaitingListDAO();

    /**
     * Singleton thread-safe tramite Initialization-on-demand holder idiom.
     */
    class Holder {
        private static final DAOFactory INSTANCE = new PostgresDAOFactory();
    }

    static DAOFactory getDAOFactory() {
        return Holder.INSTANCE;
    }
}
