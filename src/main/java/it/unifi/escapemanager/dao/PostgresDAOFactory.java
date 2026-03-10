package it.unifi.escapemanager.dao;

public class PostgresDAOFactory implements DAOFactory {

    @Override
    public StanzaDAO getStanzaDAO() {
        return new StanzaDAOPostgres();
    }

    @Override
    public PrenotazioneDAO getPrenotazioneDAO() {
        return new PrenotazioneDAOPostgres();
    }
}
