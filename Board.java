/** @author Afonso deSousa && Miguel Victorino */

/** class to store the game configuration (number of tiles, and tiles with actions); */
public class Board {

    private final String[] tiles;
    final String EMPTY = "EMPTY";
    private final String BIRD = "BIRD";


    /**@param squares: number of tiles in specified board;
     fills board with EMPTY tiles and with BIRD tiles;*/
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
    }


    /**@param index number of tile
     * @return the content of the board on the specified tile;*/
    public String getTile(int index) {
        return tiles[index];
    }

    /**@param square number of cliff tile;
     * @param fine type of cliff tile;
     * changes the content of a board tile from EMPTY to number of fines;*/
    public void addFine(int square, int fine) {
        tiles[square] = String.valueOf(fine);
    }

    /** @param square number of cliff tile;
     * @param type type of cliff tile;
     * changes the content of a board tile from EMPTY to cliff type;*/
    public void addFall(int square, String type) {
        tiles[square] = type.toUpperCase();

    }

}
