package it.unifi.escapemanager.dao;

public interface DAOFactory {

    StanzaDAO getStanzaDAO();

    PrenotazioneDAO getPrenotazioneDAO();
    
    WaitingListDAO getWaitingListDAO();

    static DAOFactory getDAOFactory() {
        return new PostgresDAOFactory();
    }
}
