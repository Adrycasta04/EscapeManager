DELETE FROM LISTA_ATTESA;
DELETE FROM PRENOTAZIONE;
DELETE FROM UTENTE;
DELETE FROM STANZA;
DELETE FROM SEDE;

-- Sedi
INSERT INTO SEDE (id, nome, citta)
    VALUES ('FI01', 'Sede Firenze', 'Firenze');

-- Stanze
INSERT INTO STANZA
    (id, sede_id, tema, capienza_max,
     prezzo_base, stato_corrente, pricing_strategy)
    VALUES
    ('R01', 'FI01', 'Horror Asylum', 6,
     20.0, 'DISPONIBILE', 'BASE');

INSERT INTO STANZA
    (id, sede_id, tema, capienza_max,
     prezzo_base, stato_corrente, pricing_strategy)
    VALUES
    ('R02', 'FI01', 'Piramide Egizia', 4,
     25.0, 'DISPONIBILE', 'BASE');

-- Utenti (Clienti)
INSERT INTO UTENTE
    (id, email, password_hash, ruolo, nome, telefono)
    VALUES
    ('CLIENTE_01', 'mario.rossi@mail.it',
     'hash123', 'CLIENTE', 'Mario Rossi', '3331234567');

INSERT INTO UTENTE
    (id, email, password_hash, ruolo, nome, telefono)
    VALUES
    ('CLIENTE_02', 'luigi.verdi@mail.it',
     'hash456', 'CLIENTE', 'Luigi Verdi', '3339876543');

-- Utenti (Game Master)
INSERT INTO UTENTE
    (id, email, password_hash, ruolo, matricola)
    VALUES
    ('GM_01', 'gm1@escapemanager.it',
     'hashgm1', 'GAME_MASTER', 'GM-2026-001');

-- Utenti (Admin)
INSERT INTO UTENTE
    (id, email, password_hash, ruolo, livello_accesso)
    VALUES
    ('ADMIN_01', 'admin@escapemanager.it',
     'hashadm', 'ADMIN', 1);
