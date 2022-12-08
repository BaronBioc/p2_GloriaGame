/** @author Afonso deSousa */

/** class to store the game configuration (number of tiles, and tiles with actions); */
public class Board {

    // create a String array tiles from which we can know the tiles' actions;
    private final String[] tiles;
    private final int[] deaths;
    final String EMPTY = "EMPTY";
    private final String BIRD = "BIRD";
    private int num_deaths = 0;


    /**@param squares: number of tiles in specified board;
     */
    public Board(int squares) {
        this.tiles = new String[squares];
        for (int i = 0; i < tiles.length; i ++) {
            tiles[i] = EMPTY;
        }
        for (int x = 1; x < tiles.length - 2; x ++) {
            if ((x % 9) == 0) {
                tiles[x] = BIRD;
            }
        }

        deaths = new int[squares];
    }

    public String getTile(int index) {
        return tiles[index];
    }

    public void addFine(int square, int fine) {
        tiles[square] = String.valueOf(fine);
    }

    public void addFall(int square, String type) {
        tiles[square] = type.toUpperCase();
        if (type.equals("DEATH")) {
            deaths[num_deaths] = square;
            num_deaths ++;
        }

    }

    public void cleanDeaths() {
        for (int i = 0; i < num_deaths; i ++) {
            tiles[deaths[i]] = EMPTY;
        }

    }

    public void fillDeath() {
        for (int i = 0; i < num_deaths; i ++) {
            if (deaths[i] != 0)  {
                tiles[deaths[i]] = "DEATH";
            }
        }

    }

}
