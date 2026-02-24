# EscapeManager

Progetto Java per la gestione di un sistema Escape Room con architettura MVC e persistenza PostgreSQL.

## Struttura
- `database/`: script SQL (`schema.sql`, `default.sql`, `reset.sql`)
- `docs/UML/`: diagrammi UML o Mermaid
- `latex/`: relazione tecnica e immagini
- `src/main/java/it/unifi/escapemanager/`: codice applicativo
- `src/test/java/it/unifi/escapemanager/`: test

## Avvio rapido
1. Configurare PostgreSQL.
2. Eseguire `database/schema.sql` e `database/default.sql`.
3. Compilare con Maven:
   ```bash
   mvn clean test
   ```
