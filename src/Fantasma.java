public class Fantasma {
    char Color;
    private int[] CoordAttuali;
    private int[] CoordSpawn;
    private String Behaviour = "";

    // Last direction moved: {deltaRow, deltaCol}
    // Used to prevent ghosts from reversing into the tile they just came from.
    private int[] ultimaDirezione = {0, 0};

    public Fantasma(char color) {
        this.Color = color;
        switch (Color) {
            case 'P': this.CoordSpawn = new int[]{11, 11}; break;
            case 'B': this.CoordSpawn = new int[]{11, 12}; break;
            case 'O': this.CoordSpawn = new int[]{11, 14}; break;
            default:  this.CoordSpawn = new int[]{11, 15}; break;
        }
    }

    // getters
    public int[] getCoordAttuali()   { return CoordAttuali; }
    public int[] getCoordSpawn()     { return CoordSpawn; }
    public String getBehaviour()     { return Behaviour; }
    public char getColor()           { return Color; }
    public int[] getUltimaDirezione(){ return ultimaDirezione; }

    // setters
    public void setCoordAttuali(int[] coordAttuali) { CoordAttuali = coordAttuali; }
    public void setBehaviour(String behaviour)       { Behaviour = behaviour; }
    public void setUltimaDirezione(int[] dir)        { ultimaDirezione = dir; }
}
