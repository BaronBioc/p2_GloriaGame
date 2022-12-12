/** @author Afonso deSousa && Miguel Victorino */

/** class to operate the players according to the Main.java inputs through Player.java and Board.java methods; */
public class GameSystem {

    private final int squares;
    private final Board board;
    private final String NUM_1 = "1";
    private final String NUM_2 = "2";
    private final String NUM_3 = "3";
    private final String NUM_4 = "4";
    private final String BIRD = "BIRD";
    private final String CRAB = "CRAB";
    private final String HELL = "HELL";
    private final String DEATH = "DEATH";
    private final Player[] ranking;
    private final Player[] players;
    private int active;
    private final int size;
    private boolean hasEliminated;
    private boolean startPoint;
    private int next = 0;


    /** @param entry player name sequence;
     * @param squares number of tiles on game board;
     */
    public GameSystem(String entry, int squares) {
        this.squares = squares;
        this.size = entry.length();
        board = new Board(squares);
        active = size;
        players = new Player[size];
        ranking = new Player[size];
        for (int i = 0; i < size; i ++) {
            players[i] = new Player(String.valueOf(entry.charAt(i)), i);
            ranking[i] = players[i];
        }

        startPoint = true;
    }


    /** eliminates the furthest player in case no elimination has occurred
     * and if the cup is still ongoing resets all players' positions and fines;*/
    public void startNewGame() {

        if (!hasEliminated) {
            eliminateLast();
        }

        if (isCupOn()) {
            for (int x = 0; x < size; x ++) {
                players[x].setNewGame();
            }
            next = 0;
            getNextPlayer();
            startPoint = true;
            hasEliminated = false;
        }

    }

    /** eliminates the player furthest from the winning tile; */
    private void eliminateLast() {
        int last = 0;
        for (int i = 0; i < active; i ++) {
            for (int j = i + 1; j < active; j ++) {
                if (ranking[j].compare(ranking[last]) < 0) {
                    last = j;
                }
            }
        }

        ranking[last].eliminate();
        active --;
        moveToBottom(ranking[last]);
    }

    /** @return total number of players. */
    public int numPlayers() {
        return size;
    }

    /** @param index index of player in the ranking list.
     * @return the player ranked at specified place; */
    public Player getRank(int index) {
        return ranking[index];
    }

    /** @param square the square of the fine tile
     * @param fines the number of fines of the fine tile
     * adds a fine tile to the object board;
     * */
    public void addFine(int square, int fines) {
        board.addFine(square, fines);
    }

    /** @param square the square of the cliff tile
     * @param type the cliff type of the cliff house
     * adds a cliff tile to the object board;
     * */
    public void addFall(int square, String type) {
        board.addFall(square, type);

    }

    /** @return the game winner. */
    public Player getWinner() {
        return ranking[0];
    }

    /** @return if the number of active players is bigger than 1; */
    public boolean isCupOn() {
        return (active > 1);
    }

    /** @param target letter of the player requested;
     * pick the corresponding Player from the player list by the letter input;
     */
    public Player selectPlayer(String target) {
        Player selected = null;

        for (int i = 0; i < size; i ++) {
            if (target.equals(players[i].getName())) {
                selected = players[i];
            }
        }

        return selected;
    }

    /** return the next Player to play keeping the order and fines; */
    public Player getRunnerUp() {
        Player runnerUp = null;
        int idx = next;
        int[] fines = new int[size];
        for (int i = 0; i < size; i ++) {
            fines[i] = players[i].getFines();
        }

        while (runnerUp == null) {
            if (!players[idx].isEliminated()) {
                if (fines[idx] != 0) {
                    fines[idx] --;
                }
                else {
                    runnerUp = players[idx];
                }
            }
            idx ++;
            if (idx == size) {
                idx = 0;
            }
        }

        return runnerUp;
    }

    /** checks if the next player can play and if not determines the next player without fines; */
    private void getNextPlayer() {

        while (!players[next].canPlay()) {
            if (!players[next].isEliminated() ) {
                if (players[next].hasFines()) {
                    players[next].cutFines();
                }
            }
            next ++;
            if (next == size) {
                next = 0;
            }

        }

    }

    /** @param player selected player from selectPLayer();
     * check if Player has fines
     * @return if the player has fines
     */
    public boolean getStatus(Player player) {
        return !player.hasFines();
    }

    /** @param dice1 number of first dice;
     * @param dice2 number of second dice;
     */
    public void playRound(int dice1, int dice2) {
        Player turnPlayer = players[next];

        if (startPoint && ((dice1 == 6 && dice2 == 3) || (dice1 == 3 && dice2 == 6)))
            turnPlayer.lucky(squares);
        else {
            int sum = dice1 + dice2;
            turnPlayer.moveDice(sum, squares);
            runSquare(turnPlayer, sum);
        }

        if (turnPlayer.getPosition() == squares) {
            turnPlayer.addWin();
            startNewGame();
        }
        else {
            if (active == 1) {
                ranking[0].addWin();
            }
            next ++;
            if (next == size) {
                next = 0;
                startPoint = false;
            }
            getNextPlayer();

        }

        sortRank();



    }

    /** executes the dice and check in which square the player is and what the square does */
    public void runSquare(Player player, int sum) {
        String action = board.getTile(player.getPosition());
        switch (action) {
            case NUM_1: player.setFines(1); break;
            case NUM_2: player.setFines(2); break;
            case NUM_3: player.setFines(3); break;
            case NUM_4: player.setFines(4); break;
            case BIRD: player.applyBird(squares); break;
            case CRAB: player.applyCrab(sum); break;
            case HELL: player.applyHell(); break;
            case DEATH: killPlayer(player); break;
        }

    }

    /**@param eli player that lands on a death tile
     *  if no player has been eliminated before player eli is eliminated and ranking is updated; */
    private void killPlayer(Player eli) {
        if (!hasEliminated) {
            eli.eliminate();
            active--;

            moveToBottom(eli);

            hasEliminated = true;
        }

    }

    /** @param eli player eliminated
     * changes the selected player rank to the top of eliminated players; */
    private void moveToBottom(Player eli) {
        int idx = 0;
        for (int i = 0; i < size; i ++) {
            if (eli == ranking[i]) {
                idx = i;
            }
        }

        for (int x = idx; x < active; x ++) {
            ranking[x] = ranking[x + 1];
        }

        ranking[active] = eli;
    }

    /** sorts the active player ranking based on wins, position and playing order; */
    private void sortRank() {
        for (int i = 0; i < active - 1; i ++) {
            for (int j = i + 1; j < active; j ++) {
                if (ranking[i].rank(ranking[j]) < 0) {
                    Player plyr = ranking[j];
                    ranking[j] = ranking[i];
                    ranking[i] = plyr;
                }

            }
        }

    }

}





