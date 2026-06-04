public class Pacman {
    private int[] CoordAttuali;
    public int[] CoordSpawn;
    private int vite=3;
    private int[] Previus;

   public Pacman(){
       this.CoordSpawn = new int[]{18, 13};
   }

    // getters
    public int getVite() {
        return vite;
    }

    public int[] getCoordAttuali() {
        return CoordAttuali;
    }

    public int[] getCoordSpawn() {
        return CoordSpawn;
    }

    public int[] getPrevius() {
        return Previus;
    }

    // setters
    public void setCoordAttuali(int[] coordAttuali) {
        CoordAttuali = coordAttuali;
    }

    public void setVite(int vite) {
        this.vite = vite;
    }

    public void setPrevius(int[] previus) {
        Previus = previus;
    }
}
