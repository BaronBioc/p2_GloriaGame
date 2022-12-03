/** @author Afonso deSousa */

/** class to store the game configuration (number of tiles, and tiles with actions); */
public class Board {

    // create a String array tiles from which we can know the tiles' actions;
    private final String[] tiles;
    private final String EMPTY = "EMPTY";
    private final String BIRD = "BIRD";

    /** @param tiles string array with the board's tiles information;
     * @param fine number of the tiles where there are fines;
     * @param fall number of the tiles where there are falls;*/
    public Board(int squares) {
        this.tiles = new String[squares];
        for (int i = 0; i < tiles.length; i ++) {
            tiles[i] = EMPTY;
        }
        for (int c = 1; c < tiles.length - 2; c ++) {
            if ((c % 9) == 0) {
                tiles[c] = BIRD;
            }
        }

    }

    public String getTile(int index) {
        return tiles[index];
    }

    public void addFine(int square, int fine) {
        tiles[square] = String.valueOf(fine);
    }

    public void addFall(int square, String type) {
        tiles[square] = type.toUpperCase();
    }


}
