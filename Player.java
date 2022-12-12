/** @author Afonso deSousa && Miguel Victorino */

/** class to store each Player's information (name, fines, position) and change them according to the game progress;
 all players start on square 1 and with 0 fines; */
public class Player {

    private final int BIRD = 9;
    private final String name;
    private final int turn;
    private int position = 1;
    private int fines = 0;
    private int wins = 0;
    private boolean eliminated;


    /** @param name name of the player;
     * @param turn playing order;*/
    public Player(String name, int turn) {
        this.name = name;
        this.turn = turn;
        eliminated = false;
    }


    /** resets position and fines for a new game start;*/
    public void setNewGame() {
        position = 1;
        fines = 0;
    }

    /** @return the player's designated name/letter; */
    public String getName() {
        return name;
    }

    /** @return the player's current position; */
    public int getPosition() {
        return position;
    }

    /** @return Player's current position/tile; */
    public int getFines() {
        return fines;
    }

    /** @return if player has fines or not; */
    public boolean hasFines() {
        return fines > 0;
    }

    /**@param fines number of fines of a fine tile;
     *  add fines to the player; */
    public void setFines(int fines) {
        this.fines = fines;
    }

    /** remove one fine; */
    public void cutFines() {
        fines --;
    }

    /** @return if player has no fines and is not eliminated; */
    public boolean canPlay() {
        return !hasFines() && !eliminated;
    }

    /** @param dice sum of both dices' numbers;
     * @param squares number of tiles of the game;
     * add the dice numbers to the player's position; */
    public void moveDice(int dice, int squares) {
        if ((position + dice) >= squares) {
            position = squares;
        }
        else {
            position += dice;
        }

    }

    /** @param square total tiles of the game;
     *  in case the player hit the lucky dice numbers he's moved to the winning tile;*/
    public void lucky(int square) {
        position = square;
    }

    /** @param squares total tiles of the game:
     * add to the Player's position the BIRD's bonus; */
    public void applyBird(int squares) {
        if ((position + BIRD) > squares) {
            position = squares;
        }
        else {
            position += BIRD;
        }

    }

    /** @param dice sum of both dices' numbers
     * moves the player back according to the crab's penalty;*/
    public void applyCrab(int dice) {
        if (position > 2 * dice) {
            position -= 2 * dice;
        }
        else {
            position = 1;
        }

    }

    /** moves player to the starting tile; */
    public void applyHell() {
        position = 1;
    }

    /** changes player's eliminated status to true, disabling him from playing; */
    public void eliminate() {
        eliminated = true;
    }

    /** @return number of wins; */
    public int getWins() {
        return wins;
    }

    /** increases number of wins;*/
    public void addWin() {
        wins ++;
    }

    /**@return if player is eliminated; */
    public boolean isEliminated() {
        return eliminated;
    }

    /** @param other player to compare;
     * compares the player with another one based on wins, position and playing order;*/
    public int rank(Player other) {
        int comp = wins - other.wins;
        if (comp == 0) {
            comp = position - other.position;
            if (comp == 0) {
                comp = other.turn - turn;
            }
        }

        return comp;
    }

    /** @param other player to compare;
     * compares player based only on positions and playing order;*/
    public int compare(Player other) {
        int comp = position - other.position;
        if (comp == 0) {
            comp = other.turn - turn;
        }

        return comp;
    }

}
