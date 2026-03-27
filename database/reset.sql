-- Reset completo del database (usato anche da DatabaseHelper.resetTestDatabase() nei test)
-- L'ordine di DROP rispetta le dipendenze (FK)

DROP TABLE IF EXISTS LISTA_ATTESA;
DROP TABLE IF EXISTS PRENOTAZIONE;
DROP TABLE IF EXISTS UTENTE;
DROP TABLE IF EXISTS STANZA;
DROP TABLE IF EXISTS SEDE;
