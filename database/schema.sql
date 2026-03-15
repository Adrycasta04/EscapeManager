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

-- ========================================
-- INDICI PER OTTIMIZZAZIONE PERFORMANCE
-- ========================================

-- Indici per query frequenti su PRENOTAZIONE
CREATE INDEX idx_prenotazione_cliente ON PRENOTAZIONE(cliente_id);
CREATE INDEX idx_prenotazione_stanza ON PRENOTAZIONE(stanza_id);
CREATE INDEX idx_prenotazione_stanza_data ON PRENOTAZIONE(stanza_id, data_ora);
CREATE INDEX idx_prenotazione_data_ora ON PRENOTAZIONE(data_ora);

-- Indici per query sulla LISTA_ATTESA
CREATE INDEX idx_lista_attesa_stanza ON LISTA_ATTESA(stanza_id);
CREATE INDEX idx_lista_attesa_cliente ON LISTA_ATTESA(cliente_id);

-- Indici per ricerche su STANZA
CREATE INDEX idx_stanza_sede ON STANZA(sede_id);
CREATE INDEX idx_stanza_sede_stato ON STANZA(sede_id, stato_corrente);
CREATE INDEX idx_stanza_stato ON STANZA(stato_corrente);

-- Indice per login rapido su UTENTE
CREATE INDEX idx_utente_email ON UTENTE(email);
CREATE INDEX idx_utente_ruolo ON UTENTE(ruolo);

-- Indice per ricerche su SEDE
CREATE INDEX idx_sede_citta ON SEDE(citta);

-- ========================================
-- CONSTRAINT AGGIUNTIVI PER BUSINESS RULES
-- ========================================

-- Vincolo: numero giocatori deve essere positivo
ALTER TABLE PRENOTAZIONE ADD CONSTRAINT chk_numero_giocatori
CHECK (numero_giocatori > 0);

-- Vincolo: prezzo totale non negativo
ALTER TABLE PRENOTAZIONE ADD CONSTRAINT chk_prezzo_totale
CHECK (prezzo_totale >= 0);

-- Vincolo: capienza massima stanza positiva
ALTER TABLE STANZA ADD CONSTRAINT chk_capienza_max
CHECK (capienza_max > 0);

-- Vincolo: prezzo base non negativo
ALTER TABLE STANZA ADD CONSTRAINT chk_prezzo_base
CHECK (prezzo_base >= 0);