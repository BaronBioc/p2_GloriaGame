/** @author Afonso deSousa */

/** class to store each Player's information (name, fines, position) and change them according to the game progress;
 all players start on square 1 and with 0 fines; */
public class Player {

    private int position = 1;
    private int fines = 0;
    private int wins = 0;
    private boolean eliminated;
    private final int BIRD = 9;
    private final String name;


    /** @param name name of the player; */
    public Player(String name) {
        this.name = name;
        eliminated = false;
    }


    /** @param dice sum of both dices' numbers
     * @param squares number of tiles of the game
     * add the dice numbers to the Player's position; */
    public void moveDice(int dice, int squares) {
        if ((position + dice) >= squares) {
            position = squares;
        }
        else {
            position += dice;
        }

    }

    /** @return the Player's designated name/letter; */
    public String getName() {
        return name;
    }

    /** @return the Player's current position; */
    public int getPosition() {
        return position;
    }

    /** @return Player's current position/tile; */
    public int getFines() {
        return fines;
    }

    /** @return if Player has fines or not; */
    public boolean hasFines() {
        return fines > 0;
    }

    /** add fines to the Player; */
    public void setFines(int fines) {
        this.fines = fines;
    }

    /** remove one fine; */
    public void cutFines() {
        fines --;
    }

    /** @param squares number of tiles of the game
     * add to the Player's position the BIRD's bonus; */

    public void applyBird(int squares) {
        if ((position + BIRD) >= squares) {
            position = squares;
        }
        else {
            position += BIRD;
        }
    }

    /** @param dice sum of both dices' numbers
     * remove to the Player's position the FALL's penalty;
     */
    public void applyCrab(int dice) {
        if (position > 2 * dice) {
            position -= 2 * dice;
        }
        else {
            position = 1;
        }

    }

    public void applyHell() {
        position = 1;
    }

    public void eliminate() {
        eliminated = true;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        wins ++;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public int rank(Player other) {
        int sort = wins - other.wins;
        if (sort == 0) {
            sort = position - other.position;
        }

        return sort;
    }

}