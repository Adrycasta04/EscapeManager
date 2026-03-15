# 📄 Compilazione LaTeX - Multi-IDE Setup

Questo progetto può essere compilato con **IntelliJ IDEA**, **VSCode**, **Antigravity** o **CLI**.

## 🎯 Configurazione Scelta: UNIVERSALE

**Tutti i file di output (`.pdf`, `.aux`, `.log`, etc.) sono generati nella cartella `LaTeX/`.**

### ✅ Vantaggi
- Funziona con **TUTTI** gli IDE senza configurazione aggiuntiva
- Standard LaTeX universale
- Git ignora automaticamente i file temporanei (vedi `.gitignore`)

### 📁 Struttura Directory

```
LaTeX/
├── main.tex              ← Sorgente principale
├── main.pdf              ← PDF generato (sempre qui!)
├── main.aux              ← File temporanei (ignorati da git)
├── main.log              ← Log compilazione
├── main.toc              ← Table of contents
├── main.synctex.gz       ← SyncTeX (se abilitato)
├── chapters/             ← Capitoli sorgente
│   ├── 01_introduzione.tex
│   ├── 02_requisiti.tex
│   └── ...
└── images/               ← Immagini PNG (da UML)
    ├── usecase_diagram.png
    └── ...
```

## 🔧 Come Compilare

### IntelliJ IDEA
1. Apri `LaTeX/main.tex`
2. Click sul pulsante **Run** (▶️) in alto a destra
3. Trova `main.pdf` nella stessa cartella di `main.tex`

**Configurazione IntelliJ** (se richiesta):
- Compiler: `pdflatex`
- Options: `-interaction=nonstopmode -synctex=1`
- Output directory: **NON impostare** (usa default)

### VSCode (con LaTeX Workshop)
1. Apri `LaTeX/main.tex`
2. `Ctrl+Alt+B` oppure click su "Build LaTeX project"
3. PDF generato in `LaTeX/main.pdf`

**Configurazione VSCode** (`settings.json`):
```json
{
  "latex-workshop.latex.outDir": "%DIR%",
  "latex-workshop.latex.tools": [
    {
      "name": "pdflatex",
      "command": "pdflatex",
      "args": [
        "-synctex=1",
        "-interaction=nonstopmode",
        "-file-line-error",
        "%DOC%"
      ]
    }
  ]
}
```

### Antigravity
Usa le impostazioni di default, il PDF sarà in `LaTeX/main.pdf`.

### CLI (latexmk)
```bash
cd LaTeX
latexmk -pdf main.tex
# Output: main.pdf nella stessa cartella
```

### CLI (pdflatex diretto)
```bash
cd LaTeX
pdflatex -synctex=1 -interaction=nonstopmode main.tex
pdflatex -synctex=1 -interaction=nonstopmode main.tex  # 2x per TOC
```

## 🧹 Pulizia File Temporanei

```bash
cd LaTeX
# Con latexmk
latexmk -c

# Manuale
rm -f *.aux *.log *.toc *.out *.fls *.synctex.gz *.fdb_latexmk
```

**NOTA**: I file temporanei sono già ignorati da Git (vedi `.gitignore` root).

## ⚠️ Note Importanti

1. **NON creare cartella `out/`**: Ogni IDE la gestisce diversamente, meglio evitare
2. **NON usare `.latexmkrc`**: IntelliJ lo ignora, crea conflitti
3. **File `main.fdb_latexmk`**: Cache di latexmk, può essere ignorato/cancellato
4. **SyncTeX**: Se usi sync tra PDF e sorgente, il file `.synctex.gz` è normale

## 🐛 Troubleshooting

### "main.synctex(busy)" nella cartella out
- Chiudi tutti i PDF viewer
- Cancella la cartella `out/` se esiste
- Ricompila

### PDF non trovato in IntelliJ
- Verifica che Output Directory sia **vuoto** o **non impostato** nelle impostazioni
- Cerca `main.pdf` in `LaTeX/` (stessa cartella di `main.tex`)

### Errori di compilazione cross-IDE
- Chiudi tutti gli IDE
- Cancella tutti i file temporanei: `rm -f LaTeX/*.aux LaTeX/*.log LaTeX/*.toc`
- Ricompila da zero
