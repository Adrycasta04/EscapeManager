# EscapeManager

> Sistema gestionale per franchising di Escape Room — Progetto di Ingegneria del Software (A.A. 2025/2026)

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36.svg)](https://maven.apache.org/)
[![JUnit](https://img.shields.io/badge/JUnit-5-25A162.svg)](https://junit.org/junit5/)
[![Mockito](https://img.shields.io/badge/Mockito-5-green.svg)](https://site.mockito.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

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
- **Lista d'attesa** con notifiche (Observer Pattern)
- **Costruzione fluida** delle prenotazioni (Builder Pattern)
- **Persistenza PostgreSQL** con DAO Pattern e Abstract Factory
- **Testing multilivello**: Unit, Mock (Mockito), Integration, Functional
- **Documentazione completa** con diagrammi UML (PlantUML) e relazione LaTeX

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
│  dao (Persistence)                  │  ← Accesso dati via JDBC
└─────────────────────────────────────┘
```

### Design Patterns implementati

| Pattern | Ruolo nel sistema |
|---------|-------------------|
| **State** | Ciclo di vita della Stanza (Disponibile → In Corso → In Pulizia → In Manutenzione) |
| **Strategy** | Politiche di prezzo dinamiche (TariffaBase, TariffaWeekend) |
| **Observer** | Lista d'attesa con notifica ai clienti iscritti |
| **Builder** | Costruzione fluente delle Prenotazioni |
| **Singleton** | ConnectionManager per la connessione al database |
| **Abstract Factory** | DAOFactory per disaccoppiamento dalla persistenza concreta |

---

## 🛠️ Tecnologie

| Categoria | Tool/Framework |
|-----------|----------------|
| Linguaggio | Java 17 (LTS) |
| Database | PostgreSQL 15 |
| Driver | JDBC PostgreSQL 42.7 |
| Build Tool | Apache Maven 3.9 |
| Testing | JUnit 5.11 + Mockito 5.14 |
| Coverage | JaCoCo 0.8.12 |
| UML | PlantUML |
| Documentazione | LaTeX (TeX Live) |
| VCS | Git + GitHub |

---

## 📦 Requisiti

- **JDK 17** o superiore
- **PostgreSQL 15** (locale o remoto)
- **Maven** (wrapper incluso: `mvnw.cmd`)

---

## 🚀 Installazione

### 1. Clone del repository
```bash
git clone https://github.com/Adrycasta04/EscapeManager.git
cd EscapeManager
```

### 2. Setup Database PostgreSQL

Crea il database e inizializza lo schema:
```bash
createdb -U postgres escapemanager
psql -U postgres -d escapemanager -f database/reset.sql
psql -U postgres -d escapemanager -f database/schema.sql
psql -U postgres -d escapemanager -f database/default.sql
```

### 3. Configurazione connessione DB

**Opzione A** — File di configurazione (consigliato):
```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
# Modifica db.properties con le tue credenziali
```

**Opzione B** — Variabili d'ambiente:
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

**Output atteso**: `Tests run: 45, Failures: 0, Errors: 0, Skipped: 0`

### Tipologie di test

| Tipo | Descrizione |
|------|-------------|
| **Unit Test** | Domain Model isolato (State, Strategy, Observer, Builder) |
| **Mock Test** | Controller con Mockito (senza DB reale) |
| **Integration Test** | DAO su PostgreSQL reale con reset `@BeforeEach` |
| **Functional Test** | End-to-end tracciati sui template Use Case |

### Coverage Report (JaCoCo)
```bash
./mvnw.cmd test
# Report HTML generato in: target/site/jacoco/index.html
```

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
- **Sorgente**: [`LaTeX/main.tex`](LaTeX/main.tex)
- **PDF**: [`LaTeX/main.pdf`](LaTeX/main.pdf)

**Compilazione**:
```bash
cd LaTeX
latexmk -pdf main.tex
```

**Struttura della relazione**:
1. Introduzione e Stack Tecnologico
2. Analisi dei Requisiti (Use Case Diagram + Templates)
3. Progettazione Architetturale (UML + Design Pattern)
4. Implementazione e Architettura del Codice
5. Collaudo e Testing

### Diagrammi UML
- **Sorgenti PlantUML**: [`docs/UML/*.puml`](docs/UML/)
- **Immagini PNG**: [`LaTeX/images/*.png`](LaTeX/images/)

Diagrammi disponibili:
- Use Case Diagram (con `<<include>>` e `<<extend>>`)
- Domain Model (Class Diagram con tutti i Design Pattern)
- Package Diagram e Component Diagram
- ER Diagram (con notazione Crow's Foot)
- State Machine Diagram (ciclo di vita Stanza)
- Sequence Diagram (UC1, UC3, UC4)
- Object Diagram (Observer Pattern a runtime)
- Class Diagram Eccezioni

Naming convention: `fig_X_Y_nome.puml` → tracciabilità diretta con la relazione.

**Rigenerazione immagini**:
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

Distribuito sotto licenza [MIT](LICENSE).

---

## 🙏 Ringraziamenti

- **GitHub Copilot** e **Google Gemini** per AI-Assisted Pair Programming
- Materiali del corso di Ingegneria del Software (Prof. Vicario)
- PlantUML community per diagrams-as-code
