# EscapeManager

(premere Ctrl + Shift + V per visualizzarlo bene su VScode)

> Sistema gestionale per franchising di Escape Room con architettura BCE a livelli e Design Pattern GoF.

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36.svg)](https://maven.apache.org/)
[![JUnit](https://img.shields.io/badge/JUnit-5-25A162.svg)](https://junit.org/junit5/)
[![Mockito](https://img.shields.io/badge/Mockito-5-green.svg)](https://site.mockito.org/)

---

## 📋 Indice
- [Caratteristiche](#-caratteristiche)
- [Architettura](#️-architettura)
- [Tecnologie](#️-tecnologie)
- [Requisiti](#-requisiti)
- [Installazione](#-installazione)
- [Testing](#-testing)
- [Documentazione](#-documentazione)
- [Autore](#-autore)

---

## ✨ Caratteristiche

- **Gestione Prenotazioni** con calcolo prezzi dinamico (Strategy Pattern)
- **Ciclo di vita stanze** con State Machine (State Pattern)
- **Lista d'attesa** con notifiche asincrone (Observer Pattern)
- **Persistenza PostgreSQL** con DAO Pattern e Abstract Factory
- **Testing multilivello**: Unit, Mock (Mockito), Integration, Functional
- **Documentazione completa** con UML (PlantUML) e relazione LaTeX

---

## 🏗️ Architettura

Architettura **BCE** (Boundary-Control-Entity) a 4 layer:

```
┌─────────────────────────────────────┐
│  cli (Boundary)                     │  ← Interfaccia utente CLI
├─────────────────────────────────────┤
│  controllers (Control)              │  ← Orchestrazione casi d'uso
├─────────────────────────────────────┤
│  domain (Entity)                    │  ← Entità + Design Pattern
├─────────────────────────────────────┤
│  dao (Persistence)                  │  ← Object-Relational Mapping
└─────────────────────────────────────┘
```

**Design Patterns implementati**:
- **State Pattern**: Ciclo di vita della Stanza (Disponibile → In Corso → In Pulizia → In Manutenzione)
- **Strategy Pattern**: Politiche di prezzo dinamiche (TariffaBase, TariffaWeekend)
- **Observer Pattern**: Lista d'attesa con notifiche asincrone
- **Builder Pattern**: Costruzione fluente delle Prenotazioni
- **Singleton Pattern**: ConnectionManager per pool di connessioni DB
- **Abstract Factory**: DAOFactory per disaccoppiamento persistenza

---

## 🛠️ Tecnologie

| Categoria | Tool/Framework |
|-----------|----------------|
| Language | Java 17 (LTS) |
| Database | PostgreSQL 15 |
| Driver | JDBC PostgreSQL 42.x |
| Build Tool | Apache Maven 3.9 |
| Testing | JUnit 5.10 + Mockito 5.x |
| Coverage | JaCoCo 0.8.12 |
| UI Prototype | JavaFX 21 (FXML) |
| UML | PlantUML 1.2024.x |
| Documentation | LaTeX (TeX Live 2024) |
| IDE | IntelliJ IDEA / VS Code |
| VCS | Git + GitHub |

---

## 📦 Requisiti
- **JDK 17** o superiore
- **PostgreSQL 15** locale o remoto
- **Maven Wrapper** (già incluso nel progetto)
- **JavaFX** (solo per mockup UI, opzionale)

---

## 🚀 Installazione

### 1. Clone del repository
```bash
git clone https://github.com/YOUR_USERNAME/EscapeManager.git
cd EscapeManager
```

### 2. Setup Database PostgreSQL

Crea il database:
```bash
createdb -U postgres escapemanager
```

Esegui gli script SQL:
```bash
psql -U postgres -d escapemanager -f database/schema.sql
psql -U postgres -d escapemanager -f database/default.sql
```

### 3. Configurazione connessione DB

**Opzione A**: File di configurazione (consigliato)
```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
# Modifica db.properties con le tue credenziali
```

**Opzione B**: Variabili d'ambiente
```bash
export EM_DB_URL=jdbc:postgresql://localhost:5432/escapemanager
export EM_DB_USER=postgres
export EM_DB_PASSWORD=yourpassword
```

### 4. Build del progetto
```bash
./mvnw.cmd clean package
```

---

## 🧪 Testing

### Esecuzione suite completa
```bash
./mvnw.cmd test
```

**Output atteso**: `Tests run: 44, Failures: 0, Errors: 0, Skipped: 0`

### Coverage Report (JaCoCo)
```bash
./mvnw.cmd test
# Report HTML generato in: target/site/jacoco/index.html
```

### Tipologie di test

| Tipo | Count | Descrizione |
|------|-------|-------------|
| **Unit Test** | 22 | Domain Model isolato (State, Strategy, Observer, Builder) |
| **Mock Test** | 15 | Controller con Mockito (senza DB) |
| **Integration Test** | 1+ | DAO + PostgreSQL reale |
| **Functional Test** | 6 | End-to-end tracciati su Use Case |

**Coverage**: 68% instruction coverage (esclusi CLI e mockup)

---

## 💻 Esecuzione CLI

```bash
./mvnw.cmd -DskipTests package
./mvnw.cmd -DskipTests dependency:copy-dependencies
java -cp "target/classes;target/dependency/*" it.unifi.escapemanager.cli.Main
```

**Menu principale**:
```
=== EscapeManager CLI ===
1. Cliente
2. Game Master
3. Admin
0. Esci
```

---

## 📖 Documentazione

### Relazione LaTeX
- **Sorgente**: `LaTeX/main.tex`
- **PDF finale**: `LaTeX/main.pdf` (52 pagine)

**Compilazione**:
```bash
cd LaTeX
latexmk -pdf main.tex
# Output: LaTeX/main.pdf
```

**Struttura relazione**:
1. Introduzione e Stack Tecnologico
2. Analisi Requisiti (Use Case + Templates)
3. Progettazione Architetturale (UML + Design Pattern)
4. Implementazione e Architettura Codice
5. Testing e Coverage (JaCoCo)
6. Qualità Software (SOLID + ISO 25010)
7. Conclusioni e Sviluppi Futuri

### Diagrammi UML
- **Sorgenti PlantUML**: `docs/UML/*.puml`
- **Immagini PNG**: `LaTeX/images/*.png`

**Diagrammi disponibili**:
- Use Case Diagram (con `<<include>>` e `<<extend>>`)
- Domain Model (Class Diagram con Design Pattern)
- Package Diagram
- Component Diagram
- ER Diagram (Database)
- State Machine Diagram (RoomState)
- Sequence Diagram (x3: UC1, UC3, UC4)
- Activity Diagram (UC1 con flussi alternativi)

**Rigenerazione**:
```bash
java -jar plantuml.jar -tpng "docs/UML/*.puml" -o "../../LaTeX/images"
```

---

## 👨‍💻 Autore

**Adriano Luca Castaldo**
Università degli Studi di Firenze
Corso: Ingegneria del Software (A.A. 2025/2026)
Docente: Prof. Enrico Vicario

---

## 📝 Licenza

Progetto accademico - Tutti i diritti riservati

---

## 🙏 Ringraziamenti

Questo progetto è stato sviluppato con il supporto di:
- **GitHub Copilot** e **Google Gemini** per AI-Assisted Pair Programming
- Materiali del corso di Ingegneria del Software (Prof. Vicario)
- PlantUML community per diagrams-as-code
