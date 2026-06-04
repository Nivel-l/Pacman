import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static ZGL.GraficaSemplice.*;

public class Labirinto {
    private Pacman pacman = new Pacman();
    private Fantasma[] fantasmi = new Fantasma[]{
            new Fantasma('P'),
            new Fantasma('B'),
            new Fantasma('O'),
            new Fantasma('R')
    };
    public char[][] Mappa;

    // Tiles ghosts sit on underneath themselves (restored when they move away)
    private char[] TiloSottoFantasma = {' ', ' ', ' ', ' '};

    private int punteggio = 0;
    private int gradi;

    // -----------------------------------------------------------------------
    // Map loading
    // -----------------------------------------------------------------------
    public void CaricaMappa() {
        try (BufferedReader bf = new BufferedReader(new FileReader("Pacman/src/mappa.txt"))) {
            Mappa = new char[23][27];
            for (int i = 0; i < 23; i++) {
                String riga = bf.readLine();
                if (riga == null) riga = "";
                // Padda la riga a 27 caratteri se è più corta
                while (riga.length() < 27) riga += " ";
                Mappa[i] = riga.toCharArray();
            }
            setFinestra(900, 900, "PacMan");
            quadratoPieno(0.5, 0.5, 1, NERO);
        } catch (IOException e) {
            System.out.println("Errore caricamento mappa: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Rendering
    // -----------------------------------------------------------------------
    public void DisegnaMappa() {
        for (int i = 0; i < 23; i++) {
            for (int l = 0; l < 27; l++) {
                double x         = (1.0 / 27.0) * l + (1.0 / 54.0);
                double y         = (1.0 / 27.0) * (22 - i) + (1.0 / 54.0);
                double dimensione = 1.0 / 27.0;
                int[] coord      = {i, l};

                switch (Mappa[i][l]) {
                    case 'X':
                        quadratoPieno(x, y, dimensione, BLU);
                        break;
                    case '.':
                        quadratoPieno(x, y, dimensione, NERO);
                        cerchioPieno(x, y, 0.0033, Color.YELLOW);
                        break;
                    case 'G':
                        pacman.setCoordAttuali(coord);
                        pacman.setPrevius(coord);
                        quadratoPieno(x, y, dimensione, NERO);
                        disegnaImmagineRidimensionata(x, y, "Pacman/src/Player_Pacman.png", gradi, dimensione, dimensione);
                        break;
                    case 'P':
                        fantasmi[0].setCoordAttuali(coord);
                        quadratoPieno(x, y, dimensione, NERO);
                        disegnaImmagineRidimensionata(x, y, "Pacman/src/PinkGhost.png", 0, dimensione, dimensione);
                        break;
                    case 'B':
                        fantasmi[1].setCoordAttuali(coord);
                        quadratoPieno(x, y, dimensione, NERO);
                        disegnaImmagineRidimensionata(x, y, "Pacman/src/BlueGhost.png", 0, dimensione, dimensione);
                        break;
                    case 'O':
                        fantasmi[2].setCoordAttuali(coord);
                        quadratoPieno(x, y, dimensione, NERO);
                        disegnaImmagineRidimensionata(x, y, "Pacman/src/OrangeGhost.png", 0, dimensione, dimensione);
                        break;
                    case 'R':
                        fantasmi[3].setCoordAttuali(coord);
                        quadratoPieno(x, y, dimensione, NERO);
                        disegnaImmagineRidimensionata(x, y, "Pacman/src/RedGhost.png", 0, dimensione, dimensione);
                        break;
                    default:
                        quadratoPieno(x, y, dimensione, NERO);
                        break;
                }
            }
        }
        // Draw score in the top-left area (row 0 is all walls, safe to reuse)
        testo(0.05, 0.97, "Punteggio: " + punteggio + "   Vite: " + pacman.getVite());
    }

    // -----------------------------------------------------------------------
    // Game loop
    // -----------------------------------------------------------------------
    public void Gioco() {
        // Remember the last direction Pac-Man was heading so movement feels
        // smooth (held key keeps moving).
        int[] direzioneP = {0, 0};

        while (pacman.getVite() > 0) {

            // --- Pac-Man input -------------------------------------------
            int[] Pcoord = pacman.getCoordAttuali();

            if (premutoTasto(FRECCIA_SU)) {
                direzioneP = new int[]{-1, 0};
                gradi = 90;
            } else if (premutoTasto(FRECCIA_GIU)) {
                direzioneP = new int[]{1, 0};
                gradi = 270;
            } else if (premutoTasto(FRECCIA_SX)) {
                direzioneP = new int[]{0, -1};
                gradi = 180;
            } else if (premutoTasto(FRECCIA_DX)) {
                direzioneP = new int[]{0, 1};
                gradi = 0;
            }

            int nuovaR = Pcoord[0] + direzioneP[0];
            int nuovaC = Pcoord[1] + direzioneP[1];

            // Tunnel wrap-around (row 11, columns 0 and 26)
            if (nuovaC < 0)  nuovaC = 26;
            if (nuovaC > 26) nuovaC = 0;

            if (Mappa[nuovaR][nuovaC] != 'X' && Mappa[nuovaR][nuovaC] != 'D') {
                // Eat dot
                if (Mappa[nuovaR][nuovaC] == '.') {
                    punteggio += 10;
                }

                // Erase old position, write new
                Mappa[Pcoord[0]][Pcoord[1]] = ' ';
                pacman.setPrevius(Pcoord);
                pacman.setCoordAttuali(new int[]{nuovaR, nuovaC});
                Mappa[nuovaR][nuovaC] = 'G';
            }

            // --- Ghost AI ------------------------------------------------
            muoviFantasmi();

            // --- Collision check -----------------------------------------
            for (Fantasma f : fantasmi) {
                int[] fc = f.getCoordAttuali();
                int[] pc = pacman.getCoordAttuali();
                if (fc[0] == pc[0] && fc[1] == pc[1]) {
                    pacman.setVite(pacman.getVite() - 1);
                    respawn();
                    break;
                }
            }

            DisegnaMappa();

            // Slow the loop down so the game is playable
            try { Thread.sleep(75); } catch (InterruptedException ignored) {}
        }

        // Game over
        quadratoPieno(0.5, 0.5, 1, NERO);
        testo(0.3, 0.55, "GAME OVER");
        testo(0.25, 0.45, "Punteggio finale: " + punteggio);
    }

    // -----------------------------------------------------------------------
    // Ghost movement
    // -----------------------------------------------------------------------

    private void muoviFantasmi() {
        int[] pacCoord   = pacman.getCoordAttuali();
        int[] pacDir     = pacman.getPrevius() != null
                ? new int[]{ pacCoord[0] - pacman.getPrevius()[0],
                             pacCoord[1] - pacman.getPrevius()[1] }
                : new int[]{0, 0};

        for (int i = 0; i < fantasmi.length; i++) {
            Fantasma f      = fantasmi[i];
            int[] target    = calcolaTarget(i, pacCoord, pacDir);
            int[] nuovaPos  = scegliMossa(f, target);

            if (nuovaPos != null) {
                // Restore the tile the ghost was standing on
                char charGhost = ghostChar(f.getColor());
                Mappa[f.getCoordAttuali()[0]][f.getCoordAttuali()[1]] = TiloSottoFantasma[i];

                // Remember what is under the new position
                TiloSottoFantasma[i] = Mappa[nuovaPos[0]][nuovaPos[1]];
                if (TiloSottoFantasma[i] == charGhost) TiloSottoFantasma[i] = ' ';

                // Update direction memory
                f.setUltimaDirezione(new int[]{
                    nuovaPos[0] - f.getCoordAttuali()[0],
                    nuovaPos[1] - f.getCoordAttuali()[1]
                });

                f.setCoordAttuali(nuovaPos);
                Mappa[nuovaPos[0]][nuovaPos[1]] = charGhost;
            }
        }
    }

    /**
     * Returns the target tile for ghost i using classic Pac-Man rules.
     *
     * Red   (i=3): target = Pac-Man's exact position  (direct chase)
     * Pink  (i=0): target = 4 tiles ahead of Pac-Man  (ambush)
     * Blue  (i=1): target = mirror of Red around 2 tiles ahead of Pac-Man
     * Orange(i=2): target = Pac-Man when far (>8), scatter corner when close
     */
    private int[] calcolaTarget(int ghostIndex, int[] pacPos, int[] pacDir) {
        switch (ghostIndex) {
            case 3: // Red — direct chase
                return new int[]{pacPos[0], pacPos[1]};

            case 0: { // Pink — 4 tiles ahead
                int tr = clampRow(pacPos[0] + pacDir[0] * 4);
                int tc = clampCol(pacPos[1] + pacDir[1] * 4);
                return new int[]{tr, tc};
            }

            case 1: { // Blue — vector trick: 2 ahead of Pac, then mirror around Red
                int midR = clampRow(pacPos[0] + pacDir[0] * 2);
                int midC = clampCol(pacPos[1] + pacDir[1] * 2);
                int[] redPos = fantasmi[3].getCoordAttuali();
                int tr = clampRow(midR + (midR - redPos[0]));
                int tc = clampCol(midC + (midC - redPos[1]));
                return new int[]{tr, tc};
            }

            case 2: { // Orange — chase when far, scatter to bottom-left corner
                int[] oPos = fantasmi[2].getCoordAttuali();
                double dist = distanza(oPos, pacPos);
                if (dist > 8) {
                    return new int[]{pacPos[0], pacPos[1]};
                } else {
                    return new int[]{22, 0}; // bottom-left scatter corner
                }
            }

            default:
                return new int[]{pacPos[0], pacPos[1]};
        }
    }

    /**
     * Picks the legal neighbour tile that is closest to target,
     * excluding the tile the ghost just came from (no reversal).
     * Ghosts cannot pass through walls ('X') or the ghost-house door ('D').
     */
    private int[] scegliMossa(Fantasma f, int[] target) {
        int[] pos     = f.getCoordAttuali();
        int[] ultimaDir = f.getUltimaDirezione();

        // Four candidate directions: up, down, left, right
        int[][] direzioni = {{-1,0},{1,0},{0,-1},{0,1}};

        int[] bestPos  = null;
        double bestDist = Double.MAX_VALUE;

        for (int[] d : direzioni) {
            // No reversing
            if (d[0] == -ultimaDir[0] && d[1] == -ultimaDir[1]) continue;

            int nr = pos[0] + d[0];
            int nc = pos[1] + d[1];

            // Tunnel wrap
            if (nc < 0)  nc = 26;
            if (nc > 26) nc = 0;

            // Bounds check
            if (nr < 0 || nr >= 23) continue;

            char tile = Mappa[nr][nc];
            if (tile == 'X' || tile == 'D') continue;

            double dist = distanza(new int[]{nr, nc}, target);
            if (dist < bestDist) {
                bestDist = dist;
                bestPos  = new int[]{nr, nc};
            }
        }
        return bestPos;
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    /** Euclidean distance between two grid positions. */
    private double distanza(int[] a, int[] b) {
        int dr = a[0] - b[0];
        int dc = a[1] - b[1];
        return Math.sqrt(dr * dr + dc * dc);
    }

    private int clampRow(int r) { return Math.max(0, Math.min(22, r)); }
    private int clampCol(int c) { return Math.max(0, Math.min(26, c)); }

    private char ghostChar(char color) {
        switch (color) {
            case 'P': return 'P';
            case 'B': return 'B';
            case 'O': return 'O';
            default:  return 'R';
        }
    }

    /** Puts Pac-Man and all ghosts back at their spawn positions. */
    private void respawn() {
        // Clear Pac-Man's current tile
        int[] pc = pacman.getCoordAttuali();
        Mappa[pc[0]][pc[1]] = ' ';
        int[] spawnP = pacman.getCoordSpawn();
        pacman.setCoordAttuali(spawnP.clone());
        pacman.setPrevius(spawnP.clone());
        Mappa[spawnP[0]][spawnP[1]] = 'G';

        // Clear and respawn each ghost
        char[] ghostChars = {'P','B','O','R'};
        for (int i = 0; i < fantasmi.length; i++) {
            int[] gc = fantasmi[i].getCoordAttuali();
            if (gc != null) Mappa[gc[0]][gc[1]] = TiloSottoFantasma[i];
            int[] spawnF = fantasmi[i].getCoordSpawn();
            fantasmi[i].setCoordAttuali(spawnF.clone());
            fantasmi[i].setUltimaDirezione(new int[]{0, 0});
            TiloSottoFantasma[i] = ' ';
            Mappa[spawnF[0]][spawnF[1]] = ghostChars[i];
        }

        // Brief pause after death
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }
}
