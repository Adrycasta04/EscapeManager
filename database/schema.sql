-- Pulizia iniziale (utile per i test e per resettare lo stato)
DROP TABLE IF EXISTS LISTA_ATTESA;
DROP TABLE IF EXISTS PRENOTAZIONE;
DROP TABLE IF EXISTS UTENTE;
DROP TABLE IF EXISTS STANZA;
DROP TABLE IF EXISTS SEDE;

-- 1. Tabella SEDE
CREATE TABLE SEDE (
    id VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    citta VARCHAR(100) NOT NULL
);

-- 2. Tabella STANZA
CREATE TABLE STANZA (
    id VARCHAR(50) PRIMARY KEY,
    sede_id VARCHAR(50) NOT NULL,
    tema VARCHAR(100) NOT NULL,
    capienza_max INT NOT NULL,
    prezzo_base DECIMAL(10, 2) NOT NULL,
    stato_corrente VARCHAR(50) NOT NULL, -- Es: 'DISPONIBILE', 'IN_MANUTENZIONE'
    pricing_strategy VARCHAR(50) NOT NULL, -- Es: 'BASE', 'WEEKEND'
    FOREIGN KEY (sede_id) REFERENCES SEDE(id) ON DELETE CASCADE
);

-- 3. Tabella UTENTE (Single Table Inheritance)
CREATE TABLE UTENTE (
    id VARCHAR(50) PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    ruolo VARCHAR(20) NOT NULL, -- 'CLIENTE', 'GAME_MASTER', 'ADMIN'
    
    -- Attributi Nullable (specifici per ruolo)
    nome VARCHAR(100),          -- Per Cliente
    telefono VARCHAR(20),       -- Per Cliente
    matricola VARCHAR(50),      -- Per Game Master
    livello_accesso INT         -- Per Admin
);

-- 4. Tabella PRENOTAZIONE
CREATE TABLE PRENOTAZIONE (
    id VARCHAR(50) PRIMARY KEY,
    cliente_id VARCHAR(50) NOT NULL,
    stanza_id VARCHAR(50) NOT NULL,
    data_ora TIMESTAMP NOT NULL,
    numero_giocatori INT NOT NULL,
    prezzo_totale DECIMAL(10, 2) NOT NULL,
    stato_partita VARCHAR(20) NOT NULL, -- 'CONFERMATA', 'CONCLUSA', 'ANNULLATA'
    FOREIGN KEY (cliente_id) REFERENCES UTENTE(id),
    FOREIGN KEY (stanza_id) REFERENCES STANZA(id)
);

-- 5. Tabella LISTA_ATTESA (Observer Pattern N:N)
CREATE TABLE LISTA_ATTESA (
    cliente_id VARCHAR(50) NOT NULL,
    stanza_id VARCHAR(50) NOT NULL,
    data_richiesta TIMESTAMP NOT NULL,
    PRIMARY KEY (cliente_id, stanza_id, data_richiesta),
    FOREIGN KEY (cliente_id) REFERENCES UTENTE(id) ON DELETE CASCADE,
    FOREIGN KEY (stanza_id) REFERENCES STANZA(id) ON DELETE CASCADE
);