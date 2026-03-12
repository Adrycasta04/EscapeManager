# EscapeManager

Applicativo Java per la gestione operativa di una catena di Escape Room con CLI, persistenza PostgreSQL e documentazione LaTeX.

## Requisiti
- JDK 25
- PostgreSQL locale
- Maven Wrapper (gia' incluso)
- JavaFX (solo per i mockup UI)

## Database
Il progetto usa PostgreSQL con schema e dati di esempio.

Script:
- Schema: `database/schema.sql`
- Dati di default: `database/default.sql`

Esempio con psql:
```powershell
psql -U postgres -d escapemanager -f database/schema.sql
psql -U postgres -d escapemanager -f database/default.sql
```

Configurazione DB:
- File `src/main/resources/db.properties` (vedi `db.properties.example` per il formato)
- Oppure variabili d'ambiente: `EM_DB_URL`, `EM_DB_USER`, `EM_DB_PASSWORD`

## Test
I test usano il database configurato in `db.properties`/variabili d'ambiente e fanno TRUNCATE delle tabelle.

```powershell
mvnw.cmd test
```

## Esecuzione CLI
Compila ed esegui la CLI:
```powershell
mvnw.cmd -DskipTests package
mvnw.cmd -DskipTests dependency:copy-dependencies
java -cp "target/classes;target/dependency/*" it.unifi.escapemanager.cli.Main
```

## UML e Relazione
- Diagrammi sorgente: `docs/UML/*.puml`
- Immagini usate in LaTeX: `LaTeX/images/*.png`
- Sorgente relazione: `LaTeX/main.tex`
- PDF relazione: `LaTeX/main.pdf`

Per rigenerare le immagini UML usa PlantUML (es. estensione VS Code) aprendo i file `.puml` e esportando in PNG.
