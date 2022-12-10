/** @author Afonso deSousa && Miguel Victorino */

/** class to store the game configuration (number of tiles, and tiles with actions); */
public class Board {

    // create a String array tiles from which we can know the tiles' actions;
    private final String[] tiles;
    private final int[] deaths;
    final String EMPTY = "EMPTY";
    private final String BIRD = "BIRD";


    /**@param squares: number of tiles in specified board;
     */
    public Board(int squares) {
        this.tiles = new String[squares + 1];
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
    /**
     * @param index of a certain square
     * @return the function of the designated square;
     */
    public String getTile(int index) {
        return tiles[index];
    }
    /**
     * @param square of the fine house
     * @param fine value of the designated fine house
     * adds to the tile at the square position the value of the fine.
     */
    public void addFine(int square, int fine) {
        tiles[square] = String.valueOf(fine);
    }
    /**
     * @param square of the cliff house
     * @param type of cliff being added
     * adds to the tile at the square position a certain cliff house.
     */
    public void addFall(int square, String type) {
        tiles[square] = type.toUpperCase();

    }



}
