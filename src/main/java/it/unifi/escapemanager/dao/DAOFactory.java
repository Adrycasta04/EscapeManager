package it.unifi.escapemanager.dao;

public interface DAOFactory {

    StanzaDAO getStanzaDAO();

    PrenotazioneDAO getPrenotazioneDAO();

    static DAOFactory getDAOFactory() {
        return new PostgresDAOFactory();
    }
}
