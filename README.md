# Pac-Man in Java

Riproduzione del classico videogioco PacMan sviluppata interamente in Java

---

## Come si gioca

| Tasto | Azione |
|-------|--------|
| ⬆️ Freccia Su | Muovi Pac-Man in su |
| ⬇️ Freccia Giù | Muovi Pac-Man in giù |
| ⬅️ Freccia Sinistra | Muovi Pac-Man a sinistra |
| ➡️ Freccia Destra | Muovi Pac-Man a destra |

**Obiettivo:** mangia tutti i puntini nel labirinto senza farti catturare dai 4 fantasmi.  
Ogni puntino vale **10 punti**. Hai **3 vite** a disposizione.

---

## I Fantasmi

Ogni fantasma ha un comportamento unico ispirato al gioco originale del 1980:

| Fantasma | Nome | Comportamento |
|----------|------|---------------|
| 🔴 Rosso | Blinky | Insegue direttamente Pac-Man |
| 🩷 Rosa | Pinky | Punta 4 celle avanti rispetto alla direzione di Pac-Man |
| 🔵 Blu | Inky | Bersaglio complesso basato sulla posizione di Blinky (il più imprevedibile) |
| 🟠 Arancio | Clyde | Insegue da lontano, si ritira nell'angolo quando è vicino |

---

## Struttura del progetto

```
Pacman/
└── src/
    ├── Main.java          → Avvio del programma
    ├── Labirinto.java     → Motore di gioco, rendering, AI fantasmi
    ├── Pacman.java        → Classe del giocatore
    ├── Fantasma.java      → Classe dei fantasmi
    ├── mappa.txt          → Configurazione del labirinto (23×27)
    └── *.png              → Sprite del gioco
```

---

## Requisiti

- **Java** 11 o superiore
- **Libreria ZGL** (`GraficaSemplice`) configurata nel classpath

---

## Come eseguire

1. Clona il repository:
   ```bash
   git clone https://github.com/tuo-username/pacman-java.git
   ```
2. Aggiungi la libreria ZGL al progetto (o configura il classpath).

---

